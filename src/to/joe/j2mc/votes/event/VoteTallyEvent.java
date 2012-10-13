package to.joe.j2mc.votes.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VoteTallyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    
    private Object o;
    
    public VoteTallyEvent(Object object) {
        o = object;
    }
    
    public Object getObject() {
        return o;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
