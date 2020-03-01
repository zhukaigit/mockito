import com.zk.common.model.Person;
import com.zk.common.model.PersonType;
import com.zk.common.model.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * 对应mock子模块下的MockAndPowermockCompareDemo类
 * 参考链接：https://blog.csdn.net/qisibajie/article/details/79068086 <br/>
 * Created by zhukai on 2020/3/1
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Student.class, PersonType.class })
public class MockAndPowermockCompareDemo {

    /**
     * 1. Mock Interface，这是正常的Mock，mock()函数可以调用mockito的，也可以调用powermock的。
     */
    @Test
    public void testMockInterface() {
        Person person = mock(Person.class);
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

    /**
     * 3.Mock Final
     * Class，这种Mock就必须使用powermock了，而且使用powermock，需要在测试类上面加两个注解。@PrepareForTest里面的类，
     * 就是你要mock的方法所在的类。
     */
    @Test
    public void testMockFinalMethod() {
        Student student = mock(Student.class);
        String mockFinalMethod = "mockFinalMethod";
        when(student.getFinalMethod()).thenReturn(mockFinalMethod);

        assertThat(student.getFinalMethod(), is(mockFinalMethod));
    }

    /**
     * 4.Mock Private Method，这种Mock也必须使用powermock，我在下面演示的代码使用了spy，
     * 这是因为spy是之后是部分mock， 这里我只想mock getPrivateMethod(), 而不想Mock
     * callPrivateMethod。但是mock是会把类里面的所有的方法都重新构造， 这样就达不到测试private method的目的了
     */
    @Test
    public void testMockPrivateMethod() throws Exception {
        Student student = spy(new Student());
        String mockPrivateMethod = "mockPrivateMethod";

        when(student, "getPrivateMethod").thenReturn(mockPrivateMethod);

        assertThat(student.callPrivateMethod(), is(mockPrivateMethod));
    }

    // 第二种是使用stub的方式去Mock private的方法，这里就不详细阐述这种方式了，我将在后面的文章里面详细介绍这种用法。
    @Test
    public void testMockPrivateMethodWithStub() throws Exception {
        Student student = new Student();
        String mockPrivateMethod = "mockPrivateMethod";

        stub(MemberMatcher.method(Student.class, "getPrivateMethod")).toReturn(mockPrivateMethod);

        assertThat(student.callPrivateMethod(), is(mockPrivateMethod));
    }

    /**
     * 5.Mock Static Method，这种方式也必须使用powermock.
     */
    @Test
    public void testMockStaticMethod() {
        mockStatic(Student.class);
        String mockStaticMethod = "mockStaticMethod";
        when(Student.getStaticMethod()).thenReturn(mockStaticMethod);

        assertThat(Student.getStaticMethod(), is(mockStaticMethod));
        assertThat(new Student().callStaticMethod(), is(mockStaticMethod));
    }

    /**
     * 6.Mock Constructor，这种方式也需要依赖powermock.
     */
    @Test
    public void testMockConstructMethod() throws Exception {
        Student student1000 = new Student(1000);
        whenNew(Student.class).withArguments(10).thenReturn(student1000);

        Student student10 = new Student(10);

        assertThat(student10.getAge(), is(1000));
    }
}
