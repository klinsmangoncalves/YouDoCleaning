package br.com.kmg.youdocleaning.model;

public class AppConfig {

    private String contactNumberWhatsApp;
    private String contactNumberCall;
    private Boolean qrCodeSoundEnabled;

    public String getContactNumberWhatsApp() {
        return contactNumberWhatsApp;
    }

    public void setContactNumberWhatsApp(String contactNumberWhatsApp) {
        this.contactNumberWhatsApp = contactNumberWhatsApp;
    }

    public Boolean getQrCodeSoundEnabled() {
        return qrCodeSoundEnabled;
    }

    public void setQrCodeSoundEnabled(Boolean qrCodeSoundEnabled) {
        this.qrCodeSoundEnabled = qrCodeSoundEnabled;
    }

    public String getContactNumberCall() {
        return contactNumberCall;
    }

    public void setContactNumberCall(String contactNumberCall) {
        this.contactNumberCall = contactNumberCall;
    }
}
