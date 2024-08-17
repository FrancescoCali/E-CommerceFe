package com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.UserRequest;

public interface iUserService {
     void createUser(UserRequest req);
     void updateUser(UserRequest req);
     void removeUser(UserRequest req);
}
