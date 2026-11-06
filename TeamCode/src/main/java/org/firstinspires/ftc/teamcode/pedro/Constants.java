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
                Controller primaryTranslationalForward = Controller.proportional(0.2632907823442158);
                Controller secondaryTranslationalForward = Controller.proportional(0.09727890881691328);
                Controller primaryTranslationalLateral = Controller.proportional(0.4212705707654059);
                Controller secondaryTranslationalLateral = Controller.proportional(0.15564821934085166);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.01568474643430331));
                c.brake.set(Controller.proportionalFeedforward(0.013332034469157815));

                c.headingFeedback.set(Controller.proportional(3.7882106537013076));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04498783807524529, 0.003445088121626618));

                c.linearBrakeCoefficients.set(Matrix.diag(0.07837283441611693, 0.06588177368022363));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0011208245988029285, 0.0013581696604074217));

                c.maxAchievableForwardVelocity.set(67.16965372994169);
                c.maxAchievableStrafeVelocity.set(54.82876035549026);
                c.naturalForwardDeceleration.set(35.76971890779682);
                c.naturalStrafeDeceleration.set(72.9903463197801);
            }
    );
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("odo_pod");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-6.781104380690207);
        c.yPodOffset.set(4.1719064186877155);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
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