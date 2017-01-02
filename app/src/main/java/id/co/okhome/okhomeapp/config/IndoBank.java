package id.co.okhome.okhomeapp.config;

import id.co.okhome.okhomeapp.R;

/**
 * Created by josong on 2016-12-20.
 */

public enum IndoBank {
    MANDIRI("MANDIRI", "BMRI", R.drawable.logo_mandiri),
    BNI("BNI", "BNIN", R.drawable.logo_bni),
    BCA("BCA", "CENA", R.drawable.logo_bca),
    MAY("Maybank", "IBBK", R.drawable.logo_maybank),
    PERMATA("Permata", "BBBA", R.drawable.logo_permata),
    HANA("Hana Bank", "HNBN", R.drawable.logo_mandiri);

    public String bankName, bankCode;
    public int logoId;

    IndoBank(String bankName, String bankCode, int logoId) {
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.logoId = logoId;
    }

}