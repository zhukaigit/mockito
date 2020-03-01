import com.zk.common.model.Person;
import com.zk.common.model.Student;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by zhukai on 2020/3/1
 *
 * 对应power模块下的MockAndPowermockCompareDemo类
 */
public class MockAndPowermockCompareDemo {

    /**
     * 1. Mock Interface，这是正常的Mock，mock()函数可以调用mockito的，也可以调用powermock的。
     */
    @Test
    public void testMockInterface() {
        Person person = Mockito.mock(Person.class);
        String mockPersonGetName = "mockPersonGetName";
        when(person.getName()).thenReturn(mockPersonGetName);
        assertThat(person.getName(), is(mockPersonGetName));
    }

    /**
     * Mock Normal Class, 这也是正常的mock, 可以使用mockito或者是powermock的mock方法。
     */
    @Test
    public void testMockNormalMethod() {
        Student student = mock(Student.class);
        String mockStudentGetName = "mockStudentGetName";
        when(student.getName()).thenReturn(mockStudentGetName);

        assertThat(student.getName(), is(mockStudentGetName));
    }


}
