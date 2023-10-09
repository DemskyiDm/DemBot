package org.BotSasSE.reactController.handleChangeAdres;

import org.BotSasSE.reactController.ResponseMessage;
import org.BotSasSE.reactController.repository.AdresRepository;
import org.BotSasSE.reactController.repository.PracownikAdresRepository;
import org.BotSasSE.reactController.repository.WorkerRepository;
import org.BotSasSE.reactController.tableEntity.Adres;
import org.BotSasSE.reactController.tableEntity.PracownikAdres;
import org.BotSasSE.reactController.tableEntity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class handlerAllAdres {
    private WorkerRepository workerRepository;
    private AdresRepository adresRepository;
    private PracownikAdresRepository pracownikAdresRepository;


    @Autowired
    public handlerAllAdres(AdresRepository AdresRepository) {
        this.adresRepository = AdresRepository;
    }

    @GetMapping("/alladreslist")
    public ResponseEntity<List<Adres>> getAllAdresses() {
        List<Adres> adreses = adresRepository.findAll();
        return new ResponseEntity<>(adreses, HttpStatus.OK);
    }


}





