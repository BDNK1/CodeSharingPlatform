package platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;


@RestController
public class CodeController {

    private final CodeService service;

    @Autowired
    public CodeController( CodeService service) {
        this.service = service;
    }

    @GetMapping("/api/code/{id}")
    @ResponseBody
    public Map<String,Object> get(@PathVariable UUID id){
        Code code = service.getById(id);
        return Map.of("code",code.getCode(),"date",code.getDate().toString(),"time",code.getTime(),"views",code.getViews());
    }

    @GetMapping("/code/{id}")
    @ResponseBody
    public ModelAndView render(@PathVariable UUID id){
        ModelAndView modelAndView = new ModelAndView();
        Code code = service.getById(id);

        modelAndView.setViewName("code");
        modelAndView.addObject("timeSecret", code.isTimeSecret());
        modelAndView.addObject("viewSecret", code.isViewSecret());
        modelAndView.addObject("code", code.getCode());
        modelAndView.addObject("date", code.getDate());
        modelAndView.addObject("time", code.getTime());
        modelAndView.addObject("views", code.getViews());

        return modelAndView;
    }

    @PostMapping(value = "/api/code/new",consumes = "application/json",produces = "application/json")
    public Map<String,String> addCode(@RequestBody Code code){
        return Map.of("id",String.valueOf(service.postCode(code.getCode(),code.getTime(),code.getViews()).getId()));
    }

    @GetMapping( "/code/new")
    public ModelAndView addCode(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("create");
        return modelAndView;
    }

    @GetMapping( "/api/code/latest")
    public List<Map<String,?>> returnLatest(){
        List<Code> allPostsList = new ArrayList<>();
        service.getAllPosts().forEach(allPostsList::add);
        Collections.reverse(allPostsList);
        return allPostsList.stream().filter(x-> !x.isTimeSecret() && !x.isViewSecret()).limit(10)
                .map(x->Map.of("code",x.getCode(),"date",x.getDate(),"time",x.getTime(),"views",x.getViews()))
                .collect(Collectors.toList());

    }

    @GetMapping("/code/latest")
    public ModelAndView renderLatest(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("latest");
        List<Code> allPostsList = new ArrayList<>();
        service.getAllPosts().forEach(allPostsList::add);
        Collections.reverse(allPostsList);

        List<Map<String,String>> latestPostsMap = allPostsList.stream().filter(x-> !x.isTimeSecret()&&!x.isViewSecret()).limit(10)
                .map(x->Map.of(x.getCode(),x.getDate().toString()))
                .collect(Collectors.toList());
        modelAndView.addObject("list",latestPostsMap);
        return modelAndView;
    }
}
