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
    
    public Object getObject() {
        return o;
    }
    
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
