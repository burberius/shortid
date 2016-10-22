package net.troja.application.servicea;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Test;

public class IdConversionServiceTest {
    private static final String ALL_FF = "0o,[_+2WS@";
    private final IdConversionService classToTest = new IdConversionService();

    @Test
    public void convert() {
        UUID uuid = UUID.randomUUID();
        String result = classToTest.convert(uuid);
        assertThat(result.length(), equalTo(20));

        uuid = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");
        result = classToTest.convert(uuid);
        assertThat(result, equalTo(ALL_FF + ALL_FF));

        uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
        result = classToTest.convert(uuid);
        assertThat(result, equalTo("00000000000000000000"));
    }

    @Test
    public void longToString() {
        assertThat(classToTest.longToString(0l), equalTo("0000000000"));
        assertThat(classToTest.longToString(84l), equalTo("000000000:"));
        assertThat(classToTest.longToString(7224l), equalTo("00000000::"));
        assertThat(classToTest.longToString(440725l), equalTo("000000000Z"));
        assertThat(classToTest.longToString(231616946283203124l), equalTo("0:::::::::"));
        assertThat(classToTest.longToString(-1), equalTo(ALL_FF));
    }
}
