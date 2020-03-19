package com.disha.restapi;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.StringJoiner;
import java.util.ArrayList;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException; 

@RestController
class InventoryResourceController {

    InventoryItemCollection itemCollection = null;

    @Autowired
    public InventoryResourceController(InventoryItemCollection itemCollection) {
        this.itemCollection = itemCollection;
    }

    @GetMapping(value = "/ims/details/{id}", produces = "application/json")
    public String getItemDetails(@PathVariable Integer id) {
        JSONResult result = new JSONResult();

        try {
            String details = itemCollection.getItem(id).toString();
            result.setResultObject(details);
        }
        catch (NullPointerException e) {
            result.setErrorDescription("Id given is invalid...");
        }
        catch (Exception e){
            result.setErrorDescription("Unexpected error...");
            e.printStackTrace();
        }

        return result.toString();
    }

    @PostMapping(value = "/ims/add", produces = "application/json")
    public String addItem(@RequestParam("name") String name, @RequestParam("description") String description) {
        JSONResult result = new JSONResult();

        try {
            itemCollection.addItem(name, description);
            result.setErrorStatus(false);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setErrorDescription("Unable to add item...");
        }

        return result.toString();
    }

    @DeleteMapping(value = "/ims/remove/{id}", produces = "application/json")
    public String removeItem(@PathVariable Integer id) {
        JSONResult result = new JSONResult();

        try {
            boolean removed = itemCollection.removeItem(id);
            if (removed) {
                result.setErrorStatus(false);
            }
            else {
                result.setErrorDescription("Item ID does not exist...");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setErrorDescription("Unexpected error...");
        }

        return result.toString();
    }

    @PostMapping(value = "/ims/log", produces = "application/json")
    public String logDetailsWithinTimeRange(@RequestParam("start") String start, @RequestParam(value="end", defaultValue="") String end) {
        JSONResult result = new JSONResult();

        try {
            LocalDateTime startDate = parseDate(start);
            LocalDateTime endDate = parseDate(end);

            if (startDate != null) {
                if (endDate == null) {
                    endDate = LocalDateTime.now();
                }
    
                ArrayList<InventoryItemCollection.ItemAndActivity> activities = itemCollection.getItemActivity(startDate, endDate);
    
                StringJoiner joiner = new StringJoiner(",", "[", "]");
                for (InventoryItemCollection.ItemAndActivity activity : activities) {
                    joiner.add(activity.toString());
                }
    
                result.setResultObject(joiner.toString());
            }
            else {
                result.setErrorDescription("Unable to parse start date provided...");
            }
        }
        catch (DateTimeParseException e){
            result.setErrorDescription("Unable to parse dates provided...");
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setErrorDescription("Unexpected error...");
        }

        return result.toString();
    }

    LocalDateTime parseDate(String source) throws DateTimeParseException {
        LocalDateTime result = null;
        boolean hasDash = source.contains("-");
        boolean hasColon = source.contains(":");

        if (hasDash && hasColon) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            result = LocalDateTime.parse(source, formatter);
        }
        else if (hasDash) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(source, formatter);
            result = LocalDateTime.of(date, LocalDateTime.now().toLocalTime());
        }
        else if (hasColon) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime time = LocalTime.parse(source, formatter);
            result = LocalDateTime.of(LocalDate.now(), time);
        }

        return result;
    }
}