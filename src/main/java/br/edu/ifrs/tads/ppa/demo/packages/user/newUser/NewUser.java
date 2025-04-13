package br.edu.ifrs.tads.ppa.demo.packages.user.newUser;

import java.util.List;

import br.edu.ifrs.tads.ppa.demo.packages.profile.Profile;
import br.edu.ifrs.tads.ppa.demo.packages.profile.Profile.AccountType;

public record NewUser(
        String name,
        String handle,
        String email,
        String password,
        String company,
        Profile.AccountType type,
        List<String> roles
)  {

}
