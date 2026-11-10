package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h) {
        // return new Follower(Drivetrain, Localizer, Foresight);
        return null;
    }
    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.27788987861986786);
                Controller secondaryTranslationalForward = Controller.proportional(0.10267288479572975);
                Controller primaryTranslationalLateral = Controller.proportional(0.41055220080668947);
                Controller secondaryTranslationalLateral = Controller.proportional(0.15168806804122595);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.01625262186130174));
                c.brake.set(Controller.proportionalFeedforward(0.01381472858210648));

                c.headingFeedback.set(Controller.proportional(4.21356234391386));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04281825934703592, 0.004370372752725941));

                c.linearBrakeCoefficients.set(Matrix.diag(0.056893992041223126, 0.048896201171615875));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0018875948110122966, 0.0015846880648316222));

                c.maxAchievableForwardVelocity.set(64.34220403428154);
                c.maxAchievableStrafeVelocity.set(52.789160430565275);
                c.naturalForwardDeceleration.set(31.832929845743475);
                c.naturalStrafeDeceleration.set(74.82763415582788);
            }
    );
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("odo_pod");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-3.3238685788132076);
        c.yPodOffset.set(-6.8815516674612445);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("fl_motor");
        c.frontRightName.set("fr_motor");
        c.backLeftName.set("bl_motor");
        c.backRightName.set("br_motor");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);

        c.manualBrakeMode.set(true);
    });
}