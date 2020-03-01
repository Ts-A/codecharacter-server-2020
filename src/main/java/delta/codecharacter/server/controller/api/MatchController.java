package delta.codecharacter.server.controller.api;

import delta.codecharacter.server.controller.response.Match.MatchResponse;
import delta.codecharacter.server.service.MatchService;
import delta.codecharacter.server.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Controller
@RequestMapping(value = "/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping(value = "/top/{PageNo}/{PageSize}")
    public ResponseEntity<List<MatchResponse>> getTopMatches(@PathVariable @NotEmpty Integer PageNo, @PathVariable @NotEmpty Integer PageSize) {
        PageUtils.validatePaginationParams(PageNo, PageSize);
        return new ResponseEntity<>(matchService.getTopMatches(PageNo, PageSize), HttpStatus.OK);
    }

}