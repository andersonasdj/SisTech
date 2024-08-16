package br.com.techgol.app.bruteforce;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.repository.FuncionarioRepository;

@Service
public class DefaultBruteForceProtectionService implements BruteForceProtectionService {

    @Value("${sistech.security.failedlogin.count}")
    private int maxFailedLogins;
 
    @Autowired
    private FuncionarioRepository userRepository;

    private int cacheMaxLimit = 2000;

    private final ConcurrentHashMap<String, FailedLogin> cache;

    public DefaultBruteForceProtectionService() {
        this.cache = new ConcurrentHashMap<>(cacheMaxLimit); //setting max limit for cache
    }
    
    public int registerLoginFailure(String username) {
    	
        Funcionario user = userRepository.buscarPorUsername(username);
        
        if(user!=null && user.getAtivo()){
            int failedCounter = user.getTentativasLogin();
            if(failedCounter < 1) { 
            	failedCounter = 1;
            	user.setTentativasLogin(failedCounter);
            }else {
            	failedCounter++;
            	user.setTentativasLogin(failedCounter);
            }
            user.setTentativasLogin(failedCounter);
            if(maxFailedLogins < failedCounter+1){
                user.setAtivo(false);; //disabling the account
            }
            else{
                //let's update the counter
                user.setTentativasLogin(failedCounter);
            }
            userRepository.save(user);
            return failedCounter;
        }
        return 0;
    }

    public void resetErroSenhaContador(String username) {
    	Funcionario user = userRepository.buscarPorUsername(username);
        if(user!=null) {
            user.setTentativasLogin(0);
            userRepository.save(user);
        }
    }

    public boolean isBruteForceAttack(String username) {
        Funcionario user =getUser(username);
        if(user !=null){
            return  user.getTentativasLogin()>=maxFailedLogins ? false: true;
        }
        return false;
    }

    protected FailedLogin getFailedLogin(final String username){
        FailedLogin failedLogin = cache.get(username.toLowerCase());

        if(failedLogin ==null){
            //setup the initial data
            failedLogin = new FailedLogin(0, LocalDateTime.now());
            cache.put(username.toLowerCase(), failedLogin);
            if(cache.size() > cacheMaxLimit){

                // add the logic to remve the key based by timestamp
            }
        }
        return failedLogin;
    }

    private Funcionario getUser(final String username){
        return  userRepository.findBynomeFuncionario(username);
    }

    public int getMaxFailedLogins() {
        return maxFailedLogins;
    }

    public void setMaxFailedLogins(int maxFailedLogins) {
        this.maxFailedLogins = maxFailedLogins;
    }

    public class FailedLogin{

        private int count;
        private LocalDateTime date;

        public FailedLogin() {
            this.count = 0;
            this.date = LocalDateTime.now();
        }

        public FailedLogin(int count, LocalDateTime date) {
            this.count = count;
            this.date = date;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }
    }
}
