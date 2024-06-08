package org.superdata.medismart;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import org.superdata.medismart.service.SysUserService;
import org.superdata.medismart.utils.file.FileUtils;
import org.superdata.medismart.utils.file.MimeTypeUtils;
import org.superdata.medismart.utils.security.SecurityUtils;

import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class TestProfile {
    @Resource
    private SysUserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void uploadAvatar() throws Exception {
        MultipartFile file = toMultipartFile(new File("D:\\Projects\\IDEAProjects\\MediSmart\\src\\test\\java\\resources\\avatar.png"));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("avatarfile", file);
       // String requestBody = jsonObject.toJSONString();

//        mockMvc.perform(MockMvcRequestBuilders.post("/avatar")
//                        .content(requestBody)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
        String avatar = FileUtils.upload("avatar", file, MimeTypeUtils.IMAGE_EXTENSION);

    }

    @Test
    public void updatePassword(){
        String oldPassword = "123456";
        String newPassword = "12345678";
        String userName = "jackson";
        newPassword = SecurityUtils.encryptPassword(newPassword);
        userService.resetUserPwd(userName, newPassword);
    }



    private MultipartFile toMultipartFile(File file)
    {
        return new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return file.getName();
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return file.length();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return Files.newInputStream(file.toPath());
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                FileUtils.copyFile(file, dest);
            }
        };
    }
}
