package org.BotSasSE.reactController.handlerAddWorker;

import java.text.ParseException;

import org.BotSasSE.reactController.ResponseMessage;
import org.BotSasSE.reactController.tableEntity.Worker;
import org.BotSasSE.reactController.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class handlerAddWorker {
    private WorkerRepository workerRepository;

    @Autowired
    public handlerAddWorker(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    @RequestMapping("/addworkerbd")
    public ResponseEntity<ResponseMessage> addWorker(
            @RequestBody Worker worker,
            Model model) {
        try {
            worker.setEmployee_date_of_birth(calculateDateBirth(worker.getEmployee_pesel()));
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
            e.printStackTrace();  // Додайте цей рядок для виведення деталей помилки
            model.addAttribute("error", "Błąd przy dodaniu pracwonika: " + e.getMessage());
            return new ResponseEntity<>(new ResponseMessage("ErrorPage"), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    public String calculateDateBirth(String employee_pesel) throws ParseException {
        String year;
        String month;
        String day;
        String birthDateConcat;
        String dateBeforeMillenium = "19";
        String dateAfterMillenium = "20";

        if (Integer.parseInt(employee_pesel.substring(2, 4)) > 13) {
            year = dateAfterMillenium.concat(employee_pesel.substring(0, 2));
            month = String.valueOf((Integer.parseInt(employee_pesel.substring(2, 4)) - 20));
        } else {
            year = dateBeforeMillenium.concat(employee_pesel.substring(0, 2));
            month = employee_pesel.substring(2, 4);
        }

        day = employee_pesel.substring(4, 6);
        birthDateConcat = year.concat("-").concat(month).concat("-").concat(day);

        return birthDateConcat;

    }


}
