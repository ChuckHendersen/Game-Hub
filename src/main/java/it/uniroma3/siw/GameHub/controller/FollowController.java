package it.uniroma3.siw.GameHub.controller;

import it.uniroma3.siw.GameHub.exceptions.InvalidFollowException;
import it.uniroma3.siw.GameHub.exceptions.UserNotFoundException;
import it.uniroma3.siw.GameHub.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @GetMapping("/newFollow/{Ua_id}/{Ub_id}")
    public String newFollow(@PathVariable("Ua_id") Long aId, @PathVariable("Ub_id") Long bId, Model model) {
        try {
            this.followService.aFollowsB(aId, bId);
            return "redirect:/user/"+bId;
        } catch (UserNotFoundException e) {
            model.addAttribute("messaggioErrore", e.getMessage());
            return "user.html";
        }catch (InvalidFollowException e) {
            model.addAttribute("messaggioErrore", e.getMessage());
            return "followError.html";
        }
    }

    @GetMapping("/deleteFollow/{Ua_id}/{Ub_id}")
    public String deleteFollow(@PathVariable("Ua_id") Long aId, @PathVariable("Ub_id") Long bId, Model model) {
        try {
            this.followService.aUnfollowsB(aId, bId);
            return "redirect:/user/"+bId;
        } catch (UserNotFoundException e) {
            model.addAttribute("messaggioErrore", e.getMessage());
            return "user.html";
        } catch (InvalidFollowException e) {
            model.addAttribute("messaggioErrore", e.getMessage());
            return "followError.html";
        }
    }
}
