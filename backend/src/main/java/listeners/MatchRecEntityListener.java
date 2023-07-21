package listeners;

import com.matchgetit.backend.entity.MatchRecEntity;
import jakarta.persistence.PreRemove;

public class MatchRecEntityListener {
    @PreRemove
    public void preRemove(MatchRecEntity matchRecEntity){
        matchRecEntity.setMember(null);
    }
}
