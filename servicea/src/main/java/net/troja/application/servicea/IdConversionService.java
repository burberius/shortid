package net.troja.application.servicea;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class IdConversionService {
    private static final BigInteger TWO_COMPL_REF = BigInteger.ONE.shiftLeft(64);
    private static final BigInteger NUM85 = BigInteger.valueOf(85);
    private final String alphabet = "0123456789" + "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "!#$%&()[]{}*+-._/@~|,;:";

    public String convert(final UUID id) {
        final String s1 = longToString(id.getLeastSignificantBits());
        final String s2 = longToString(id.getMostSignificantBits());
        return s1 + s2;
    }

    public String longToString(final long number) {
        final StringBuilder builder = new StringBuilder();
        BigInteger workNum = BigInteger.valueOf(number);
        if (workNum.compareTo(BigInteger.ZERO) < 0) {
            workNum = workNum.add(TWO_COMPL_REF);
        }
        while ((workNum.compareTo(NUM85) >= 0)) {
            final int figure = workNum.remainder(NUM85).intValue();
            builder.append(alphabet.charAt(figure));
            workNum = workNum.divide(NUM85);
        }
        builder.append(alphabet.charAt(workNum.intValue()));
        if (builder.length() < 10) {
            final char[] fill = new char[10 - builder.length()];
            Arrays.fill(fill, '0');
            builder.insert(0, fill);
        }
        return builder.toString();
    }
}
