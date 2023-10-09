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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class handlerEditADresPr {
    private WorkerRepository workerRepository;
    private AdresRepository adresRepository;
    private PracownikAdresRepository pracownikAdresRepository;


    @Autowired
    public handlerEditADresPr(PracownikAdresRepository pracownikAdresRepository) {
        this.pracownikAdresRepository = pracownikAdresRepository;
    }

    @GetMapping("/adreslist")
    public ResponseEntity<List<PracownikAdres>> getAllAdresPr() {
        List<PracownikAdres> adrespr = pracownikAdresRepository.findAll();

        for (PracownikAdres adres : adrespr) {
            Worker pracownik = adres.getPracownik();
            Adres adresD = adres.getAdres();
            String firstName = pracownik.getEmployee_first_name();
            String lastName = pracownik.getEmployee_last_name();
            String town = adresD.getResidence_city();
            String street = adresD.getResidence_street();
            String home = adresD.getResidence_home();
            String flat = adresD.getResidence_flat();
            String room = adresD.getResidence_room();
            adres.setEmployee_first_name(firstName);
            adres.setEmployee_last_name(lastName);
            adres.setResidence_city(town);
            adres.setResidence_street(street);
            adres.setResidence_home(home);
            adres.setResidence_flat(flat);
            adres.setResidence_room(room);

        }

        return new ResponseEntity<>(adrespr, HttpStatus.OK);
    }


    @RequestMapping("/workersAdres/{id}")
    public ResponseEntity<ResponseMessage> EditOrder(@RequestBody PracownikAdres pracownikAdres,
                                                     Model model) {

        try {
            pracownikAdresRepository.save(pracownikAdres);

            System.out.println("sadsad");

            return ResponseEntity.ok(new ResponseMessage("SendToDB111"));
        } catch (Exception e) {
            model.addAttribute("error", "Błąd: " + e.getMessage());
            return new ResponseEntity<>(new ResponseMessage("ErrorPage"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping("/workersAddAdres")
    public ResponseEntity<ResponseMessage> AddAdress(@RequestBody PracownikAdres pracownikAdres, Adres adres,
                                                     Model model) {

        try {

            pracownikAdresRepository.save(pracownikAdres);

            model.addAttribute("employee_id", pracownikAdres.getEmployee_id());
            model.addAttribute("residence_address_id",pracownikAdres.getAdres().getResidence_address_id());
            model.addAttribute("start_date", pracownikAdres.getStart_date());
            model.addAttribute("end_date", pracownikAdres.getEnd_date());

            System.out.println("sadsad");

            return ResponseEntity.ok(new ResponseMessage("SendToDB111"));
        } catch (Exception e) {
            model.addAttribute("error", "Błąd: " + e.getMessage());
            return new ResponseEntity<>(new ResponseMessage("ErrorPage"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}





