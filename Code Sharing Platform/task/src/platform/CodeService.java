package platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CodeService {

    private final CodeRepository codeRepository;

    @Autowired
    public CodeService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    public Code postCode(String code, Long time, int views) {
        return codeRepository.save(new Code(code, time, views));
    }

    public Code getById(UUID id) {
        if (codeRepository.existsById(id)) {
            Code code = codeRepository.findById(id).get();

            code.decrementViews();
            code.updateTime();
            if ((code.isTimeSecret()&&code.getTime()<=0)||(code.isViewSecret()&&code.getViews()<0)) {
                throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
            }
            codeRepository.save(code);
            return code;
        }
        throw new IllegalStateException("no such post");
    }


    public Iterable<Code> getAllPosts() {
        return codeRepository.findAll();
    }

}
