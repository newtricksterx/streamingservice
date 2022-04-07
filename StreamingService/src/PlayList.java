import java.util.*;

/**
 * Represents a sequence of playables to play in FIFO order.
 */
public class PlayList implements Playable {

    private List<Playable> aList = new LinkedList<>();
    private String aName;

    private Optional<PlayList> prevPlayList;
    private Optional<PlayList> nextPlayList;
    private Optional<MemorizeFunction> memorizeFunction;

    /**
     * Decorator class that handles attributes and properties of a state change function in playList
     */
    class MemorizeFunction{
        private PlayList playList;
        private Optional<Integer> functionInt;
        private Optional<Integer> index;
        private Optional<String> name;
        private Optional<Playable> changeList;

        MemorizeFunction(PlayList p){
            functionInt = Optional.ofNullable(0);
            playList = p;
        }

        MemorizeFunction(PlayList p, Playable playable){
            functionInt = Optional.ofNullable(1);
            changeList = Optional.ofNullable(playable);
            playList = p;
        }

        MemorizeFunction(PlayList p, int index){
            functionInt = Optional.ofNullable(2);
            this.index = Optional.ofNullable(index);
            playList = p;
        }

        MemorizeFunction(PlayList p, String changedName){
            functionInt = Optional.ofNullable(3);
            name = Optional.ofNullable(changedName);
            playList = p;
        }

        /**
         * calls a playList function that changes the state of that playList.
         * @throws CloneNotSupportedException
         */
        public void changeState() throws CloneNotSupportedException {
            if(functionInt.isPresent()){int f = functionInt.get();

               if(f == 0){
                   playList.shuffle();
               }
               else if(f == 1){
                   playList.addPlayable(changeList.get());
               }
               else if(f == 2){
                   if(playList.aList.size() > index.get()){
                       playList.removePlayable(index.get());
                   }
               }
               else if(f == 3){
                   playList.setName(name.get());
               }
            }
        }
    }


    /**
     * Creates a new empty playlist.
     *
     * @param pName
     *            the name of the list
     * @pre pName!=null;
     */
    public PlayList(String pName) {
        assert pName != null;
        aName = pName;

    }

    /**
     * Adds a playable at the end of this playlist.
     *
     * @param pPlayable
     *            the content to add to the list
     * @pre pPlayable!=null;
     */
    public void addPlayable(Playable pPlayable) throws CloneNotSupportedException {
        assert pPlayable != null;

        // save the current state of the playlist
        saveCurrentState();
        memorizeFunction = Optional.of(new MemorizeFunction(this, pPlayable));
        aList.add(pPlayable);
    }

    /**
     * remove a playable from the Playlist given its index
     * @param pIndex
     *          the index of playable to be removed
     * @return the removed playable
     */
    public Playable removePlayable(int pIndex) throws CloneNotSupportedException {
        assert pIndex >= 0 && pIndex < aList.size();

        // save the current state of the playlist
        saveCurrentState();
        memorizeFunction = Optional.of(new MemorizeFunction(this, pIndex));

        return aList.remove(pIndex);
    }

    /**
     * @return The name of the playlist.
     */
    public String getName() {
        return aName;
    }

    public List<Playable> getaList(){return new ArrayList<>(this.aList);}

    /**
     * modify the name of the playlist
     * @param pName
     *          the new name of the playlist
     */
    public void setName(String pName) throws CloneNotSupportedException {
        assert pName != null;

        // save the current state of the playlist
        saveCurrentState();
        memorizeFunction = Optional.of(new MemorizeFunction(this, pName));

        this.aName = pName;
    }

    /**
     * reverts the state of the playList back to its' previous state
     * @throws CloneNotSupportedException
     */
    public void undo() throws CloneNotSupportedException {
        if(prevPlayList != null && prevPlayList.isPresent()){

            // save this contents to nextPlayList
            nextPlayList = Optional.ofNullable(this.clone());

            // copy contents from prevPlayList to this
            aName = prevPlayList.get().aName;
            aList = new ArrayList<>(prevPlayList.get().aList);
            prevPlayList = prevPlayList.get().prevPlayList;

        }

    }

    /**
     * reverts the undo or calls the previous state change method
     * @throws CloneNotSupportedException
     */
    public void redo() throws CloneNotSupportedException {
        if(nextPlayList != null && nextPlayList.isPresent()){
            prevPlayList = Optional.ofNullable(this.clone());

            aName = nextPlayList.get().aName;
            aList = new ArrayList<>(nextPlayList.get().aList);
            nextPlayList = nextPlayList.get().nextPlayList;
        }
        else if(memorizeFunction != null && memorizeFunction.isPresent()){
            memorizeFunction.get().changeState();
        }
    }

    /**
     * saves the current state of the playList
     * @throws CloneNotSupportedException
     */
    private void saveCurrentState() throws CloneNotSupportedException {
        prevPlayList = Optional.ofNullable(this.clone());
        nextPlayList = Optional.empty();
    }

    /**
     * Iterating through the playlist to play playable content.
     */
    @Override
    public void play() {
        for(Playable playable:aList){
            playable.play();
        }
    }

    /**
     * change the order how playlist play the playables it contains
     */
    public void shuffle() throws CloneNotSupportedException {
        // save the current state of the playlist
        saveCurrentState();
        memorizeFunction = Optional.ofNullable(new MemorizeFunction(this));

        Collections.shuffle(aList);
    }


    /**
     * Checks is two playlists are equal based on playable objects and their order
     *
     * @param o
     *            The object to compare a playlist to
     * @return    This method returns true if the playlist is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayList playList = (PlayList) o;
        return this.aList.equals(playList.aList);
    }

    /**
     * Equal playlists have the same hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(aList);
    }

    @Override
    public PlayList clone() throws CloneNotSupportedException {
        PlayList clone = (PlayList) super.clone();
        clone.aList = new ArrayList<Playable>();
        for(Playable p : this.aList) {
            clone.aList.add(p.clone());
        }
        return clone;
    }

}
