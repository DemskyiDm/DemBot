package org.BotSasSE.reactController.handleAvanse;

import org.BotSasSE.reactController.ResponseMessage;
import org.BotSasSE.reactController.repository.AvanseRepository;
import org.BotSasSE.reactController.repository.WorkerRepository;
import org.BotSasSE.reactController.tableEntity.Adres;
import org.BotSasSE.reactController.tableEntity.Avanse;
import org.BotSasSE.reactController.tableEntity.PracownikAdres;
import org.BotSasSE.reactController.tableEntity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class handlerAvanse {
    private WorkerRepository workerRepository;
    private AvanseRepository avanseRepository;

    @Autowired
    public handlerAvanse(AvanseRepository avanseRepository) {
        this.avanseRepository = avanseRepository;
    }


    @GetMapping("/avanselist")
    public ResponseEntity<List<Avanse>> getAllAvanses() {
        List<Avanse> avansesAll = avanseRepository.findAll();

        for (Avanse avanse : avansesAll) {
            Worker pracownik = avanse.getPracownikAvanse();
            String firstName = pracownik.getEmployee_first_name();
            String lastName = pracownik.getEmployee_last_name();
            avanse.setEmployee_first_name(firstName);
            avanse.setEmployee_last_name(lastName);
        }

        return new ResponseEntity<>(avansesAll, HttpStatus.OK);
    }

    @PutMapping("/avanses/{id}")
    public ResponseEntity<ResponseMessage> EditAvanses(@RequestBody Avanse avanse,
                                                       Model model) {

        try {
            avanseRepository.save(avanse);
            System.out.println("zaliczka zmieniona");

            return ResponseEntity.ok(new ResponseMessage("SendToDB"));
        } catch (Exception e) {
            model.addAttribute("error", "Błąd: " + e.getMessage());
            return new ResponseEntity<>(new ResponseMessage("ErrorPage"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/avanses/{id}")
    public ResponseEntity<ResponseMessage> deleteAvanse(@PathVariable Integer id) {
        try {
            avanseRepository.deleteById(id);
            return ResponseEntity.ok(new ResponseMessage("Dane zaliczki pracownika usunięte"));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Błąd w trakcie usunięcia: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}





