package me.blueind.RaspberryPi;

import com.pi4j.context.Context;
import com.pi4j.Pi4J;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;

public class MotorController {
    private final Context pi4j;
    private final DigitalOutput in1, in2;

    public MotorController() {
        pi4j = Pi4J.newAutoContext();
        in1 = createOutput(17); // GPIO 17
        in2 = createOutput(27); // GPIO 27
    }

    private DigitalOutput createOutput(int address) {
        return pi4j.create(DigitalOutput.newConfigBuilder(pi4j)
                .address(address) // GPIO 핀 번호
                .shutdown(DigitalState.LOW) // 초기 상태
                .initial(DigitalState.LOW) // 초기 상태
                .build());
    }

    public void moveForward() {
        in1.high();
        in2.low();
    }

    public void moveBackward() {
        in1.low();
        in2.high();
    }

    public void stop() {
        in1.low();
        in2.low();
    }

    public void shutdown() {
        pi4j.shutdown();
    }
}
