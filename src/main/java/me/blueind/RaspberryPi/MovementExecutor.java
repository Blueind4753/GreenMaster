package me.blueind.RaspberryPi;

public class MovementExecutor {
    private final MotorController motorController;

    public MovementExecutor() {
        motorController = new MotorController();
    }

    public void execute(String command) {
        try {
            if ("CAN".equals(command)) {
                moveForwardAndReturn();
            } else if ("PLASTIC".equals(command)) {
                moveBackwardAndReturn();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void moveForwardAndReturn() throws InterruptedException {
        motorController.moveForward();
        Thread.sleep(1000);
        motorController.stop();
        Thread.sleep(500);
        motorController.moveBackward();
        Thread.sleep(1000);
        motorController.stop();
    }

    private void moveBackwardAndReturn() throws InterruptedException {
        motorController.moveBackward();
        Thread.sleep(1000);
        motorController.stop();
        Thread.sleep(500);
        motorController.moveForward();
        Thread.sleep(1000);
        motorController.stop();
    }

    public void shutdown() {
        motorController.shutdown();
    }
}
