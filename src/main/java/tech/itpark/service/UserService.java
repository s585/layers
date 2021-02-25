package tech.itpark.service;

import tech.itpark.model.*;

public interface UserService {
    UserModel register(RegistrationModel model);
    UserModel login(AuthentificationModel model);
    UserModel reset(ResetModel model);
    boolean remove(RemoveModel model);

}
