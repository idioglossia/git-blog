package lab.idioglossia.gitblog.util;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.util.FS;

public class GitBlogSshSessionFactory extends JschConfigSessionFactory{
    private final String pvkAddress;
    private final String khAddress;
    private final boolean needPassphrase;
    private String passphrase = "";

    public GitBlogSshSessionFactory(String pvkAddress, String khAddress, boolean needPassphrase, String passphrase) {
        this.pvkAddress = pvkAddress;
        this.khAddress = khAddress;
        this.needPassphrase = needPassphrase;
        if(passphrase != null)
            this.passphrase = passphrase;
    }

    @Override
    protected JSch getJSch(OpenSshConfig.Host hc, FS fs) throws JSchException {
        JSch jsch = super.getJSch(hc, fs);
        jsch.addIdentity(pvkAddress);
        jsch.setKnownHosts(khAddress);
        return jsch;
    }

    @Override
    protected void configure(OpenSshConfig.Host host, Session session) {
        session.setUserInfo(new UserInfo() {
            @Override
            public String getPassphrase() {
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
