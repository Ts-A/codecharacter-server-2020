package delta.codecharacter.server.controller.api;

import delta.codecharacter.server.service.GameService;
import delta.codecharacter.server.util.LogDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping(value = "/log/game/{gameId}")
    public ResponseEntity<LogDetails> getGameLog(@PathVariable Integer gameId) {
        return new ResponseEntity<>(gameService.getGameLog(gameId), HttpStatus.OK);
    }
}