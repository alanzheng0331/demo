package ALL_20252160A0925.D2_CONNECTION.MODEL;

import java.util.List;
import java.util.Map;

public interface IMPI_User {
    public List<Map<String,Object>> Login(String PASSWORD, String USERNAME, String PHONE) throws Exception;
    public List<Map<String,Object>> Register(String PASSWORD, String USERNAME, String PHONE) throws Exception;
}
