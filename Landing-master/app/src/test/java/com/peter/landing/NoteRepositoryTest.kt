package com.peter.landing

import androidx.paging.PagingSource
import com.peter.landing.data.local.note.Note
import com.peter.landing.data.local.note.NoteDAO
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.repository.note.NoteRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteRepositoryTest {

    private lateinit var noteDAO: NoteDAO
    private lateinit var noteRepository: NoteRepository

    @Before
    fun setup() {
        noteDAO = mockk()
        noteRepository = NoteRepository(noteDAO)
    }

    @Test
    fun checkNoteExist_returnsTrueWhenNoteExists() = runTest {
        val wordId = 100L
        coEvery { noteDAO.getNoteByWordId(wordId) } returns Note(wordId)

        val result = noteRepository.checkNoteExist(wordId)

        assertTrue(result)
    }

    @Test
    fun checkNoteExist_returnsFalseWhenNoteDoesNotExist() = runTest {
        val wordId = 101L
        coEvery { noteDAO.getNoteByWordId(wordId) } returns null

        val result = noteRepository.checkNoteExist(wordId)

        assertFalse(result)
    }

    @Test
    fun addNote_callsInsertNoteOnDAO() = runTest {
        val note = Note(200L)
        coEvery { noteDAO.insertNote(note) } just Runs

        noteRepository.addNote(note)

        coVerify { noteDAO.insertNote(note) }
    }

    @Test
    fun removeNote_callsDeleteNoteOnDAO() = runTest {
        val wordId = 300L
        coEvery { noteDAO.deleteNoteByWordId(wordId) } just Runs

        noteRepository.removeNote(wordId)

        coVerify { noteDAO.deleteNoteByWordId(wordId) }
    }

}
