import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTester {

    PlayList p1 = new PlayList("playlist 1");

    @Test
    public void defaultTest1() throws CloneNotSupportedException {
        Library.createLibrary("Library");

        assertNotNull(Library.getLibrary().getDefaultPlayable());

        Library.getLibrary().setDefaultPlayable(p1);

        PlayList playList = (PlayList) DefaultPlayable.getDefaultPlayable().clone();

        assertEquals(p1, playList);

        playList.addPlayable(p1);

        assertNotEquals(p1, playList);
    }

    @Test
    public void defaultTest2(){
        Library.createLibrary("Library");
        try{
            Library.getLibrary().setDefaultPlayable(null);
            fail();
        } catch (IllegalArgumentException e){
            // pass
        }
    }

    @Test
    public void undoredoTest1() throws CloneNotSupportedException {
        Song s1 = new Song("yo", "yo");

        p1.addPlayable(s1);
        p1.undo();

        assertTrue(p1.getaList().isEmpty());
    }

    @Test
    public void undoredoTest2() throws CloneNotSupportedException {
        Song s1 = new Song("yo", "yo");
        Song s2 = new Song("yo1", "yo1");

        p1.addPlayable(s1);
        p1.addPlayable(s2);
        p1.undo();
        p1.undo();
        p1.undo();
        p1.redo();
        p1.redo();

        assertEquals(p1.getaList().size(), 2);
        assertEquals(p1.getaList().get(0), s1);
        assertEquals(p1.getaList().get(1), s2);

    }


    @Test
    public void undoredoTest3() throws CloneNotSupportedException {
        p1.setName("teststtsts");
        p1.setName("fefwef");
        p1.setName("fsfsf");
        p1.undo();
        p1.undo();
        p1.undo();

        assertSame("playlist 1", p1.getName());
    }

    @Test
    public void undoredoTest4() throws CloneNotSupportedException {
        Song s1 = new Song("time", "a");
        Song s2 = new Song("yo", "y");
        Song s3 = new Song("abc", "def");
        p1.addPlayable(s1);
        p1.addPlayable(s2);
        p1.addPlayable(s3);

        PlayList p2 = p1.clone();

        p1.shuffle();
        p1.undo();

        assertTrue(p1.getaList().get(0).equals(p2.getaList().get(0)));
        assertTrue(p1.getaList().get(1).equals(p2.getaList().get(1)));
        assertTrue(p1.getaList().get(2).equals(p2.getaList().get(2)));
    }

    @Test
    public void undoredoTest5() throws CloneNotSupportedException {
        Song s1 = new Song("time", "a");
        Song s2 = new Song("yo", "y");
        Song s3 = new Song("abc", "def");
        p1.addPlayable(s1);
        p1.addPlayable(s2);
        p1.addPlayable(s3);

        p1.removePlayable(2);
        p1.redo();
        p1.undo();

        assertTrue(p1.getaList().size() == 3);
    }

    @Test
    public void undoredoTest6() throws CloneNotSupportedException {
        Song s1 = new Song("yo", "yo");

        p1.addPlayable(s1);
        p1.redo();

        assertEquals(2, p1.getaList().size());
        assertEquals(p1.getaList().get(0), s1);
    }

    @Test
    public void undoredoTest7() throws CloneNotSupportedException {
        Song s1 = new Song("time", "a");
        Song s2 = new Song("yo", "y");
        Song s3 = new Song("abc", "def");
        p1.addPlayable(s1);
        p1.addPlayable(s2);
        p1.addPlayable(s3);

        p1.removePlayable(0);
        p1.redo();
        p1.redo();

        assertTrue(p1.getaList().size() == 0);

        p1.undo();
        p1.undo();
        p1.undo();

        assertTrue(p1.getaList().size() == 3);
    }

    @Test
    public void undoredoTest8() throws CloneNotSupportedException {
        Song s1 = new Song("time", "a");
        Song s2 = new Song("yo", "y");
        Song s3 = new Song("abc", "def");
        p1.addPlayable(s1);
        p1.addPlayable(s2);
        p1.addPlayable(s3);

        p1.removePlayable(0);
        p1.removePlayable(0);
        p1.undo();
        p1.undo();

        p1.redo();
        p1.redo();
        p1.redo();

        assertTrue(p1.getaList().size() == 0);
    }

}
