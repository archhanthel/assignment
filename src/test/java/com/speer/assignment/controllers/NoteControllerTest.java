package com.speer.assignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speer.assignment.dto.NoteDto;
import com.speer.assignment.dto.ShareNoteDto;
import com.speer.assignment.entity.Note;
import com.speer.assignment.repository.NoteRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NoteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteController noteController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(noteController).build();
    }

    @Test
    public void getAllNotes_ShouldReturnListOfNotes() throws Exception {
        Note note1 = new Note();
        note1.setId("1");
        note1.setTitle("Note 1");
        note1.setContent("Content 1");

        Note note2 = new Note();
        note2.setId("2");
        note2.setTitle("Note 2");
        note2.setContent("Content 2");

        List<Note> notes = Arrays.asList(note1, note2);

        when(noteRepository.findAll()).thenReturn(notes);

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(notes.size()))
                .andExpect(jsonPath("$[0].id").value(note1.getId()))
                .andExpect(jsonPath("$[0].title").value(note1.getTitle()))
                .andExpect(jsonPath("$[0].content").value(note1.getContent()))
                .andExpect(jsonPath("$[1].id").value(note2.getId()))
                .andExpect(jsonPath("$[1].title").value(note2.getTitle()))
                .andExpect(jsonPath("$[1].content").value(note2.getContent()))
                .andReturn();

        verify(noteRepository, times(1)).findAll();
    }

    @Test
    public void getNoteById_ShouldReturnNote() throws Exception {
        String noteId = "1";
        Note note = new Note();
        note.setId(noteId);
        note.setTitle("Note 1");
        note.setContent("Content 1");

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));

        mockMvc.perform(get("/api/notes/{id}", noteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(note.getId()))
                .andExpect(jsonPath("$.title").value(note.getTitle()))
                .andExpect(jsonPath("$.content").value(note.getContent()))
                .andReturn();

        verify(noteRepository, times(1)).findById(noteId);
    }

    @Test
    public void getNoteById_ShouldReturnNotFound_WhenNoteNotFound() throws Exception {
        String noteId = "1";

        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/notes/{id}", noteId))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(noteRepository, times(1)).findById(noteId);
    }

    @Test
    public void createNote_ShouldReturnCreatedNote() throws Exception {
        NoteDto noteDto = new NoteDto();
        noteDto.setTitle("New Note");
        noteDto.setContent("New Content");

        Note createdNote = new Note();
        createdNote.setId("1");
        createdNote.setTitle(noteDto.getTitle());
        createdNote.setContent(noteDto.getContent());

        when(noteRepository.save(any(Note.class))).thenReturn(createdNote);

        mockMvc.perform(post("/api/notes")
                        .contentType("application/json")
                        .content("{\"title\":\"New Note\",\"content\":\"New Content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdNote.getId()))
                .andExpect(jsonPath("$.title").value(createdNote.getTitle()))
                .andExpect(jsonPath("$.content").value(createdNote.getContent()))
                .andReturn();

        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    public void updateNote_ShouldReturnUpdatedNote() throws Exception {
        String noteId = "1";
        NoteDto noteDto = new NoteDto();
        noteDto.setTitle("Updated Note");
        noteDto.setContent("Updated Content");

        Note existingNote = new Note();
        existingNote.setId(noteId);
        existingNote.setTitle("Note 1");
        existingNote.setContent("Content 1");

        Note updatedNote = new Note();
        updatedNote.setId(noteId);
        updatedNote.setTitle(noteDto.getTitle());
        updatedNote.setContent(noteDto.getContent());

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenReturn(updatedNote);

        mockMvc.perform(put("/api/notes/{id}", noteId)
                        .contentType("application/json")
                        .content("{\"title\":\"Updated Note\",\"content\":\"Updated Content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedNote.getId()))
                .andExpect(jsonPath("$.title").value(updatedNote.getTitle()))
                .andExpect(jsonPath("$.content").value(updatedNote.getContent()))
                .andReturn();

        verify(noteRepository, times(1)).findById(noteId);
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    public void updateNote_ShouldReturnNotFound_WhenNoteNotFound() throws Exception {
        String noteId = "1";
        NoteDto noteDto = new NoteDto();
        noteDto.setTitle("Updated Note");
        noteDto.setContent("Updated Content");

        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/notes/{id}", noteId)
                        .contentType("application/json")
                        .content("{\"title\":\"Updated Note\",\"content\":\"Updated Content\"}"))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(noteRepository, times(1)).findById(noteId);
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    public void deleteNote_ShouldReturnNoContent() throws Exception {
        String noteId = "1";

        mockMvc.perform(delete("/api/notes/{id}", noteId))
                .andExpect(status().isNoContent())
                .andReturn();

        verify(noteRepository, times(1)).deleteById(noteId);
    }

    @Test
    public void shareNote_ShouldReturnNoContent() throws Exception {
        String noteId = "1";
        ShareNoteDto shareNoteDto = new ShareNoteDto();
        shareNoteDto.setNoteId(noteId);
        shareNoteDto.setSharedWith("2");

        mockMvc.perform(post("/api/notes/{id}/share", noteId)
                        .contentType("application/json")
                        .content("{\"noteId\":\"1\",\"sharedWith\":\"2\"}"))
                .andExpect(status().isNoContent())
                .andReturn();

        // Verify the necessary interactions or assertions for the shareNote method
        verify(noteRepository, times(1)).findById(noteId);
        // Add more verifications as needed
    }

    @Test
    public void searchNotes_ShouldReturnMatchingNotes() throws Exception {
        String query = "example";
        Note note1 = new Note();
        note1.setId("1");
        note1.setTitle("Note 1");
        note1.setContent("Example content");

        Note note2 = new Note();
        note2.setId("2");
        note2.setTitle("Note 2");
        note2.setContent("Content");

        List<Note> matchingNotes = Arrays.asList(note1);

        when(noteRepository.findByContentContaining(query)).thenReturn(matchingNotes);

        mockMvc.perform(get("/api/notes/search")
                        .param("q", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(matchingNotes.size()))
                .andExpect(jsonPath("$[0].id").value(note1.getId()))
                .andExpect(jsonPath("$[0].title").value(note1.getTitle()))
                .andExpect(jsonPath("$[0].content").value(note1.getContent()))
                .andReturn();

        verify(noteRepository, times(1)).findByContentContaining(query);
    }


    @Test
    public void shareNote_ShouldShareNoteWithUser() throws Exception {
        String noteId = "1";
        String sharedWith = "user2";
        ShareNoteDto shareNoteDto = new ShareNoteDto();
        shareNoteDto.setSharedWith(sharedWith);
        Note existingNote = new Note();
        existingNote.setId(noteId);
        List<String> sharedWithList = new ArrayList<>();
        existingNote.setSharedWith(sharedWithList);

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(existingNote)).thenReturn(existingNote);

        mockMvc.perform(post("/api/notes/{id}/share", noteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(shareNoteDto)))
                .andExpect(status().isNoContent());

        verify(noteRepository, times(1)).findById(noteId);
        verify(noteRepository, times(1)).save(existingNote);

        List<String> updatedSharedWithList = existingNote.getSharedWith();
        assert updatedSharedWithList.contains(sharedWith);
    }

    @Test
    public void shareNote_ShouldReturnNotFound_WhenNoteNotFound() throws Exception {
        String noteId = "1";
        String sharedWith = "user2";
        ShareNoteDto shareNoteDto = new ShareNoteDto();
        shareNoteDto.setSharedWith(sharedWith);

        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/notes/{id}/share", noteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(shareNoteDto)))
                .andExpect(status().isNotFound());

        verify(noteRepository, times(1)).findById(noteId);
        verify(noteRepository, never()).save(any(Note.class));
    }

    private String asJsonString(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
