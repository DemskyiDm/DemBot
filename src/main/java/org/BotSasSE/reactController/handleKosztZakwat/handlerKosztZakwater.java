package org.BotSasSE.reactController.handleKosztZakwat;

import org.BotSasSE.reactController.ResponseMessage;
import org.BotSasSE.reactController.repository.KaryRepository;
import org.BotSasSE.reactController.repository.KosztZakwaterRepository;
import org.BotSasSE.reactController.tableEntity.Adres;
import org.BotSasSE.reactController.tableEntity.Kary;
import org.BotSasSE.reactController.tableEntity.KosztZakwater;
import org.BotSasSE.reactController.tableEntity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class handlerKosztZakwater {
    private KosztZakwaterRepository kosztZakwaterRepository;

    @Autowired
    public handlerKosztZakwater(KosztZakwaterRepository kosztZakwaterRepository) {
        this.kosztZakwaterRepository = kosztZakwaterRepository;
    }

    @GetMapping("/kosztlist")
    public ResponseEntity<List<KosztZakwater>> getAllKoszt() {
        List<KosztZakwater> kosztAll = kosztZakwaterRepository.findAll();

        for (KosztZakwater kosztZakwater : kosztAll) {
            Worker pracownik = kosztZakwater.getPracownikKosztZakw();
            Adres adresKosztZakw = kosztZakwater.getAdres();
            String firstName = pracownik.getEmployee_first_name();
            String lastName = pracownik.getEmployee_last_name();
            Double value = adresKosztZakw.getValue_accommodation();
            kosztZakwater.setEmployee_first_name(firstName);
            kosztZakwater.setEmployee_last_name(lastName);
            kosztZakwater.setValue_accommodation(value);
        }

        return new ResponseEntity<>(kosztAll, HttpStatus.OK);
    }


    @DeleteMapping("/koszty/{id}")
    public ResponseEntity<ResponseMessage> deleteAvanse(@PathVariable Long id) {
        try {
            kosztZakwaterRepository.deleteById(id);
            return ResponseEntity.ok(new ResponseMessage("Dane kosztów zakwaterowania pracownika usunięte"));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Błąd w trakcie usunięcia: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}



