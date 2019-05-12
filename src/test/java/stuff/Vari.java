package stuff;

import org.junit.Test;
import xxx.joker.libs.core.adapter.JkProcess;
import xxx.joker.libs.core.utils.JkStrings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class Vari {

    @Test
    public void aa() throws Exception {
        Path folder = Paths.get("gitTests");
//        JkFiles.delete(folder);
//        Files.createDirectories(folder);
        int res;
//        res = runCmd(folder, "git clone https://github.com/joker1618/java-8-base-parent.git j8");
//        display("git clone: {}", res);

        JkProcess.execute(folder, "git clone https://github.com/joker1618/java-8-base-parent.git j8");
//        res = runCmd(folder.resolve("j8"), "git config --global user.email \"java@mail.xyz\"");
//        display("git config mail: {}", res);
//        res = runCmd(folder.resolve("j8"), "git config --global user.name \"fake\"");
//        display("git config name: {}", res);
//
//        res = runCmd(folder.resolve("j8"), "git pull");
//        display("git pull: {}", res);
//
//        res = runCmd(folder.resolve("j8"), "git add --all");
//        display("git add: {}", res);
//        res = runCmd(folder.resolve("j8"), "git commit -m xx");
//        display("git commit: {}", res);
//        res = runCmd(folder.resolve("j8"), "git push");
//        display("git push: {}", res);
//
//        res = runCmd(folder.resolve("j8"), "git reset");
//        display("git reset: {}", res);
//        res = runCmd(folder.resolve("j8"), "git remote -v update");
//        display("git status: {}", res);
//        res = runCmd(folder.resolve("j8"), "git remote -v update");
//        display("git status: {}", res);

    }

    private int runCmd(Path folder, String cmd) throws Exception {
        ProcessBuilder pb = new ProcessBuilder()
                .command(JkStrings.splitList(cmd, " "))
                .directory(folder.toFile());

        Process p = pb.start();
        StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");

        StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");

        outputGobbler.start();

        errorGobbler.start();

        int exit = p.waitFor();

        errorGobbler.join();

        outputGobbler.join();
        return exit;
    }

    private static class StreamGobbler extends Thread {



        private final InputStream is;

        private final String type;



        private StreamGobbler(InputStream is, String type) {

            this.is = is;

            this.type = type;

        }



        @Override

        public void run() {

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

                String line;

                while ((line = br.readLine()) != null) {

                    System.out.println(type + "> " + line);

                }

            } catch (IOException ioe) {

                ioe.printStackTrace();

            }

        }

    }
}
