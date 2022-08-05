package com.pjnaik.knowyourgovernment;

import java.io.Serializable;
import java.util.HashMap;

public class Government implements Serializable {

    private String textViewJobName;
    private String textViewServingName;
    private HashMap<String, String> hasher = new HashMap<>();
    private String phonenumber;
    private String addresstext;
    private String location;
    private String websitename;
    private String webid;
    private String partyname;
    private String emaildisplay;
    private String picturelink;

    Government(String jobname, String servename, String phoneno, String addr, String weblink,
            String politicalparty, String emailid, String piclink, String locate, String socmedia) {
        textViewJobName = jobname;
        textViewServingName = servename;
        phonenumber = phoneno;
        addresstext = addr;
        location = locate;
        websitename = weblink;
        webid = socmedia;
        partyname = politicalparty;
        emaildisplay = emailid;
        picturelink = piclink;
    }

    public  Government(){ }
    public Government(String jobgovt, String namegovt){
        this.textViewJobName = jobgovt;
        this.textViewServingName = namegovt;
    }

    public String getJob() {
        return textViewJobName;
    }
    public String getName() {
        return textViewServingName;
    }
    public String getAddr(){
        return addresstext;
    }
    public void setAddr(String address){
        this.addresstext = address;
    }
    public String getEmail() {
        return emaildisplay;
    }
    public HashMap<String, String > getHasher() {
        return hasher;
    }
    public void setHasher(String key, String value) {
        hasher.put(key,value);
    }
    public void setEmail(String emails) {
        this.emaildisplay = emails;
    }
    public void setParty(String party) {
        this.partyname = party;
    }
    public String getParty() {
        return partyname;
    }
    public String getPhone() {
        return phonenumber;
    }
    public String getlocation() {return location;}
    public String getSocialId() {return webid;}
    public void setPhone(String phones) {
        this.phonenumber = phones;
    }
    public String getPicturelink() {
        return picturelink;
    }
    public String getUrl() {
        return websitename;
    }
    public void setPicturelink(String picturelink) {
        this.picturelink = picturelink;
    }
    public void setUrl(String urls) {
        this.websitename = urls;
    }

}
