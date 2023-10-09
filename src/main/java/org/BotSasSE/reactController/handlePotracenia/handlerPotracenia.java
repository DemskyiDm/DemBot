package org.BotSasSE.reactController.handlePotracenia;

import org.BotSasSE.reactController.ResponseMessage;
import org.BotSasSE.reactController.repository.AvanseRepository;
import org.BotSasSE.reactController.repository.PotraceniaRepository;
import org.BotSasSE.reactController.repository.WorkerRepository;
import org.BotSasSE.reactController.tableEntity.Avanse;
import org.BotSasSE.reactController.tableEntity.Potracenia;
import org.BotSasSE.reactController.tableEntity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class handlerPotracenia {
private PotraceniaRepository potraceniaRepository;

    @Autowired
    public handlerPotracenia(PotraceniaRepository potraceniaRepository) {
        this.potraceniaRepository =potraceniaRepository;
    }

    @RequestMapping("/addpotracenia")
    public ResponseEntity<ResponseMessage> addPotr(
            @RequestBody Potracenia potracenia,
            Model model) {
        try {
            potraceniaRepository.save(potracenia);
            return ResponseEntity.ok(new ResponseMessage("SendToDB"));
        } catch (Exception e) {
            model.addAttribute("error", "Błąd: " + e.getMessage());
            return new ResponseEntity<>(new ResponseMessage("ErrorPage"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/potracenialist")
    public ResponseEntity<List<Potracenia>> getAllPotr() {
        List<Potracenia> potraceniaAll = potraceniaRepository.findAll();

        for (Potracenia potracenia : potraceniaAll) {
            Worker pracownik = potracenia.getPracownikPotracenie();
            String firstName = pracownik.getEmployee_first_name();
            String lastName = pracownik.getEmployee_last_name();
            potracenia.setEmployee_first_name(firstName);
            potracenia.setEmployee_last_name(lastName);
        }

        return new ResponseEntity<>(potraceniaAll, HttpStatus.OK);
    }

    @PutMapping("/potracenia/{id}")
    public ResponseEntity<ResponseMessage> EditPotr(@RequestBody Potracenia potracenia,
                                                       Model model) {

        try {
            potraceniaRepository.save(potracenia);
            System.out.println("potrącenie zmienione");

            return ResponseEntity.ok(new ResponseMessage("SendToDB"));
        } catch (Exception e) {
            model.addAttribute("error", "Błąd: " + e.getMessage());
            return new ResponseEntity<>(new ResponseMessage("ErrorPage"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/potracenia/{id}")
    public ResponseEntity<ResponseMessage> deletePotr(@PathVariable Integer id) {
        try {
            potraceniaRepository.deleteById(id);
            return ResponseEntity.ok(new ResponseMessage("Dane potracenia pracownika usunięte"));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Błąd w trakcie usunięcia: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}





