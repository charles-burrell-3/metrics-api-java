package companyB.metrics.api.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidationUtils
{

    public boolean validateDateString(String dateString)
    {
        String dateRegex = "[0-9]{4}[-][0-1][0-9][-][0-3][0-9][T][0-5][0-9][:][0-5][0-9][:][0-5][0-9][:.][0-9]{1,3}";
        return validate(dateRegex, dateString);
    }

    public boolean validateEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        return validate(emailRegex, email);
    }

    private boolean validate(String regex, String string)
    {
        final AtomicBoolean found = new AtomicBoolean(true);
        if(StringUtils.isNotBlank(string))
        {
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(string);
            found.set(matcher.find());
        }
        return found.get();
    }
}
