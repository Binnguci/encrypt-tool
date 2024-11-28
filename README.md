## Crytography Tool - Java Swing Application
### Mô tả dự án:
Encryption Tool là một ứng dụng desktop được phát triển bằng Java Swing, cung cấp các tính năng mã hóa và giải mã đa dạng dựa trên các thuật toán mã hóa hiện đại và cổ điển. Công cụ này hỗ trợ mã hóa văn bản và tệp, đồng thời cung cấp các thuật toán băm và chữ ký điện tử.

Ứng dụng sử dụng thư viện giao diện FlatLaf để cung cấp trải nghiệm người dùng hiện đại, cùng với Lombok để giảm thiểu mã nguồn boilerplate. Dự án được phát triển trên Java 17.
### Các tính năng chính:
1. **Mã hóa văn bản và tệp**
    - **Mã hóa cổ điển:**
        - Shift Cipher (Caesar Cipher)
        - Vigenère Cipher
        - Affine Cipher
        - Substitution Cipher
    - **Mã hóa đối xứng:**
        - DES
        - AES
        - 3DES (Triple DES)
        - Blowfish
        - RC4
    - **Mã hóa bất đối xứng:**
        - RSA

2. **Chữ ký điện tử**
    - Thuật toán **DSA (Digital Signature Algorithm)** để tạo và kiểm tra chữ ký điện tử.

3. **Thuật toán băm**
    - Hỗ trợ các thuật toán băm tiêu chuẩn:
        - MD5
        - SHA-1
        - SHA-256
        - SHA-512
### Công nghệ sử dụng

- **Java 17**: Ngôn ngữ lập trình chính.
- **Swing**: Thư viện giao diện người dùng.
- **FlatLaf**: Cải thiện giao diện người dùng với các theme hiện đại.
- **Lombok**: Tự động tạo getter, setter và các phương thức tiện ích khác.
- **JCE (Java Cryptography Extension)**: Hỗ trợ các thuật toán mã hóa và băm.

### Hướng dẫn cài đặt

#### Yêu cầu hệ thống:
- **Java 17** hoặc cao hơn.
- IDE hỗ trợ Java như **IntelliJ IDEA**, **Eclipse**.
- **Maven** hoặc **Gradle** (tùy chọn).

#### Clone dự án từ GitHub:
Sao chép mã nguồn từ repository này:

```bash
git clone https://github.com/Binnguci/encryption-tool.git
cd encryption-tool
```
#### Cài đặt dependencies:
```bash
    mvn clean install
```
#### Chạy ứng dụng:
- Mở IntelliJ IDEA và mở dự án.
- Chạy lớp `Main` trong `src/main/java/org/midterm/Main.java`.


