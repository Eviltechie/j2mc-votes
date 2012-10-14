package to.joe.j2mc.votes.event;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VoteTallyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Object o;
    private UUID id;

    public VoteTallyEvent(UUID id, Object object) {
        o = object;
        this.id = id;
    }

    /**
     * Returns the winning {@link Object} (or {@link String}) that was passed in newVote()
     * 
     * @return The winning {@link Object} (or {@link String}) that was passed in newVote()
     */
    public Object getObject() {
        return o;
    }

    /**
     * Returns the {@link UUID} passed in newVote()
     * 
     * @return The {@link UUID} passed in newVote()
     */
    public UUID getUUID() {
        return id;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
