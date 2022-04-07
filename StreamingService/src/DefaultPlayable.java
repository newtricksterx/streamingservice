import java.util.Optional;

/**
 * Represents the default playable of the library
 */
public class DefaultPlayable implements Playable{


    private static Optional<Playable> playable;

    private static DefaultPlayable INSTANCE = null;


    private DefaultPlayable(Playable defaultPlayable){
        this.playable = Optional.ofNullable(defaultPlayable);
    }

    /**
     * creates/initializes a new instance of default playable
     * @param playable
     *                  initialize the default playable with
     */
    public static void createDefaultPlayable(Playable playable){
        if(INSTANCE == null){
            INSTANCE = new DefaultPlayable(playable);
        }
    }

    /**
     * set a default playable provided by the user.
     * @param pPlayable
     *                  the playable that the user wants to be the default playable
     */
    public static void setDefaultPlayable(Playable pPlayable){
        if(pPlayable == null){
            throw new IllegalArgumentException("tried to pass null as argument");
        }

        if(INSTANCE != null){
            playable = Optional.ofNullable(pPlayable);
        }
    }

    /**
     * get the instance of default playable
     * @return instance of default playable
     */
    public static DefaultPlayable getDefaultPlayable(){
        return INSTANCE;
    }


    @Override
    public void play() {
        if(playable.isPresent()){
            playable.get().play();
        }
    }

    @Override
    public Playable clone() throws CloneNotSupportedException {
        return playable.get().clone();
    }



}
