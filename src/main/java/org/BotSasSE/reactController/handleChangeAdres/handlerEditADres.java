/*
package org.BotSasSE.reactController.handleChangeAdres;

import org.BotSasSE.reactController.ResponseMessage;
import org.BotSasSE.reactController.repository.AdresRepository;
import org.BotSasSE.reactController.repository.PracownikAdresRepository;
import org.BotSasSE.reactController.repository.WorkerRepository;
import org.BotSasSE.reactController.tableEntity.Adres;
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
public class handlerEditADres {
    private WorkerRepository workerRepository;
    private AdresRepository adresRepository;
    private PracownikAdresRepository pracownikAdresRepository;

    @Autowired
    public handlerEditADres(AdresRepository adresRepository) {
        this.adresRepository = adresRepository;
    }



*/
/*
    @GetMapping("/11adreslist")
    public ResponseEntity<List<Adres>> getAllAdres() {
        List<Adres> adres = adresRepository.findAll();
        return new ResponseEntity<>(adres, HttpStatus.OK);
    }*//*


*/
/*
    @DeleteMapping("/workers/{id}")
    public ResponseEntity<ResponseMessage> deleteWorker(@PathVariable Integer id) {
        try {
            workerRepository.deleteById(id);
            return ResponseEntity.ok(new ResponseMessage("Dane pracownika usunięte"));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Błąd w trakcie usunięcia: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/workers/{id}")
    public ResponseEntity<ResponseMessage> EditOrder(@RequestBody Worker worker,
                                                     Model model) {

        try {
            workerRepository.save(worker);

            model.addAttribute("employee_passport_number", worker.getEmployee_passport_number());
            model.addAttribute("employee_pesel", worker.getEmployee_pesel());
            model.addAttribute("employee_last_name", worker.getEmployee_last_name());
            model.addAttribute("employee_first_name", worker.getEmployee_first_name());
            model.addAttribute("employee_gender", worker.getEmployee_gender());
            model.addAttribute("employee_position_id", worker.getEmployee_position_id());
            model.addAttribute("employee_position", worker.getEmployee_position());
            model.addAttribute("employee_status", worker.getEmployee_status());
            model.addAttribute("employee_accommodation_date", worker.getEmployee_accommodation_date());
            model.addAttribute("employee_training_date", worker.getEmployee_training_date());
            model.addAttribute("employee_medical_certificate_date", worker.getEmployee_medical_certificate_date());
            model.addAttribute("employee_document_type", worker.getEmployee_document_type());
            model.addAttribute("employee_entry_to_rp_date", worker.getEmployee_entry_to_rp_date());
            model.addAttribute("employee_legalization_document", worker.getEmployee_legalization_document());
            model.addAttribute("employee_legalization_document_expiry", worker.getEmployee_legalization_document_expiry());
            model.addAttribute("employee_contract_start", worker.getEmployee_contract_start());
            model.addAttribute("employee_contract_end", worker.getEmployee_contract_end());
            model.addAttribute("employee_phone", worker.getEmployee_phone());
            model.addAttribute("employee_last_working_day", worker.getEmployee_last_working_day());
            model.addAttribute("employee_departure_date", worker.getEmployee_departure_date());
            model.addAttribute("employee_comments", worker.getEmployee_comments());
            model.addAttribute("employee_student_card_end", worker.getEmployee_student_card_end());
            model.addAttribute("employee_date_of_birth", worker.getEmployee_date_of_birth());
            System.out.println("sadsad");

            return ResponseEntity.ok(new ResponseMessage("SendToDB"));
        } catch (Exception e) {
            model.addAttribute("error", "Błąd: " + e.getMessage());
            return new ResponseEntity<>(new ResponseMessage("ErrorPage"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

 *//*

}





*/
