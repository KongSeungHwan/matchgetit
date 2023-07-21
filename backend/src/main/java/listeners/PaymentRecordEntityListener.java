package listeners;
import com.matchgetit.backend.entity.PaymentRecordEntity;
import jakarta.persistence.PreRemove;

public class PaymentRecordEntityListener {
    @PreRemove
    public void preRemove(PaymentRecordEntity paymentRecordEntity){
        System.out.println("삭제 전 설정");
        paymentRecordEntity.setMember(null);
    }
}
