package platform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
public class Code {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "uuid-char")
    @Column(name = "id", updatable = false, nullable = false)
    @ColumnDefault("random_uuid()")
    private UUID id ;
    private String code;
    private LocalDateTime date;
    private Long time;
    private Integer views;
    @JsonIgnore
    private boolean timeSecret;
    @JsonIgnore
    private boolean viewSecret;
    @JsonIgnore
    private LocalDateTime timePassed;

    public Code() {

    }

    public Code(String code,Long time,Integer views) {
        this.code = code;
        this.time = time;
        this.views = views;
        if(time>0){
            timeSecret=true;
        }if(views>0){
            viewSecret=true;
        }
        this.date = LocalDateTime.now();
        timePassed = LocalDateTime.now();
    }

    public void updateTime(){
        if(timeSecret){
            time -= Duration.between(timePassed, LocalDateTime.now()).getSeconds();
            if(time<0){
                time=0L;
            }
            timePassed=LocalDateTime.now();
        }
    }
    public void decrementViews(){
        if(viewSecret){
            views--;
        }
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getDate() {
        return date.toLocalDate();
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isTimeSecret() {
        return timeSecret;
    }

    public void setTimeSecret(boolean timeSecret) {
        this.timeSecret = timeSecret;
    }

    public boolean isViewSecret() {
        return viewSecret;
    }

    public void setViewSecret(boolean viewSecret) {
        this.viewSecret = viewSecret;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
