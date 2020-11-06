package lab.idioglossia.gitblog.util;

import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;

public class GitBlogSshSessionFactory extends JschConfigSessionFactory{
    private final boolean needPassphrase;
    private String passphrase = "";

    public GitBlogSshSessionFactory(boolean needPassphrase, String passphrase) {
        this.needPassphrase = needPassphrase;
        if(passphrase != null)
            this.passphrase = passphrase;
    }

    @Override
    protected void configure(OpenSshConfig.Host host, Session session) {
        session.setUserInfo(new UserInfo() {
            @Override
            public String getPassphrase() {
                System.out.println("B");
                return passphrase;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public boolean promptPassword(String s) {
                return true;
            }

            @Override
            public boolean promptPassphrase(String s) {
                return needPassphrase;
            }

            @Override
            public boolean promptYesNo(String s) {
                return false;
            }

            @Override
            public void showMessage(String s) {
                System.out.println(s);
            }
        });
    }
}
