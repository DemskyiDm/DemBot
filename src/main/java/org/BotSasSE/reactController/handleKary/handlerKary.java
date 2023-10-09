package org.BotSasSE.reactController.handleKary;

import org.BotSasSE.reactController.ResponseMessage;
import org.BotSasSE.reactController.repository.KaryRepository;
import org.BotSasSE.reactController.tableEntity.Kary;
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
public class handlerKary {
private KaryRepository karyRepository;

    @Autowired
    public handlerKary(KaryRepository karyRepository) {
        this.karyRepository = karyRepository;
    }

    @RequestMapping("/addkary")
    public ResponseEntity<ResponseMessage> addKara(
            @RequestBody Kary kary,
            Model model) {
        try {
            karyRepository.save(kary);
            return ResponseEntity.ok(new ResponseMessage("SendToDB"));
        } catch (Exception e) {
            model.addAttribute("error", "Błąd: " + e.getMessage());
            return new ResponseEntity<>(new ResponseMessage("ErrorPage"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/karalist")
    public ResponseEntity<List<Kary>> getAllKara() {
        List<Kary> karyAll = karyRepository.findAll();

        for (Kary kary : karyAll) {
            Worker pracownik = kary.getPracownikKary();
            String firstName = pracownik.getEmployee_first_name();
            String lastName = pracownik.getEmployee_last_name();
            kary.setEmployee_first_name(firstName);
            kary.setEmployee_last_name(lastName);
        }

        return new ResponseEntity<>(karyAll, HttpStatus.OK);
    }

    @PutMapping("/kara/{id}")
    public ResponseEntity<ResponseMessage> EditKara(@RequestBody Kary kary,
                                                       Model model) {

        try {
            karyRepository.save(kary);
            System.out.println("kara zmieniona");

            return ResponseEntity.ok(new ResponseMessage("SendToDB"));
        } catch (Exception e) {
            model.addAttribute("error", "Błąd: " + e.getMessage());
            return new ResponseEntity<>(new ResponseMessage("ErrorPage"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/kara/{id}")
    public ResponseEntity<ResponseMessage> deleteKara(@PathVariable Integer id) {
        try {
            karyRepository.deleteById(id);
            return ResponseEntity.ok(new ResponseMessage("Dane potracenia pracownika usunięte"));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Błąd w trakcie usunięcia: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}





