package ar.com.miura.usersapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class EmailApiResponse implements Serializable  {
    public String address;
    public String status;
    public String sub_status;
    public boolean free_email;
    public Object did_you_mean;
    public String account;
    public String domain;
    public String domain_age_days;
    public String smtp_provider;
    public String mx_found;
    public String mx_record;
    public String firstname;
    public String lastname;
    public String gender;
    public Object country;
    public Object region;
    public Object city;
    public Object zipcode;
    public String processed_at;
}
