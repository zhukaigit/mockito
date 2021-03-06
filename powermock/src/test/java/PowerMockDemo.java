import org.junit.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.eq;

/**
 * Created by zhukai on 2020/3/3
 * 参考博客：https://www.cnblogs.com/Ming8006/p/6297333.html
 */
public class PowerMockDemo {

    // =============== verify 开始 ==============
    /**
     * 2.1 验证行为 - verify
     * 在单元测试中，某些特定的情况下，是有需要验证某些动作是否被执行的情况，则此时可以考虑使用Mockito.verify()来验证某个动作是否被执行。
     * 总结：基于上述示例可以 发现，verify重点要解决的是某些方法是否被调用了，调用了几次，是否带有参数以及带有何种参数。
     * 主要的应用场景就是基于特定条件的判断之后，触发某些动作，这些动作在特定情况是没有返回结果的，只能通过是否被触发执行来做相应的判断。
     */
    @Test
    public void test_verify1() {
        List<String> mockedList = PowerMockito.mock(ArrayList.class);
        mockedList.size(); //invoke the action

        //check whether it is invoked once.
        Mockito.verify(mockedList).size();

        //size() is invoked in 1 times
        Mockito.verify(mockedList, times(1)).size();
        Mockito.verify(mockedList, Mockito.atLeast(1)).size();
        Mockito.verify(mockedList, Mockito.atMost(1)).size();
    }

    @Test
    public void test_verify2() {
        List<String> mockedList = PowerMockito.mock(ArrayList.class);
        //there is no action invoked before the current statement
        verifyZeroInteractions(mockedList);
        Mockito.verify(mockedList, times(0)).size();

        //clear() is never invoked.
        Mockito.verify(mockedList, Mockito.never()).clear();

        mockedList.add("abcd");

        //do exactly checking with params.
        Mockito.verify(mockedList).add("abcd");
        Mockito.verify(mockedList).add(anyString());
    }

    @Test
    public void testActionInOrder() {
        List<String> mockedList = PowerMockito.mock(ArrayList.class);
        mockedList.size();
        mockedList.add("a b");
        mockedList.clear();

        InOrder inOrder = Mockito.inOrder(mockedList);
        inOrder.verify(mockedList).size();
        inOrder.verify(mockedList).add("a b");
        inOrder.verify(mockedList).clear();
    }
    // =============== verify 结束 ==============

    // 2.2 模拟我们所期望的结果
    @Test
    public void when_thenReturn() {
        //mock一个Iterator类
        Iterator iterator = PowerMockito.mock(Iterator.class);
        //预设当iterator调用next()时第一次返回hello，第n次都返回world
        PowerMockito.when(iterator.next()).thenReturn("hello").thenReturn("world");
        //使用mock的对象
        String result = iterator.next() + " " + iterator.next() + " " + iterator.next();
        //验证结果
        assertEquals("hello world world", result);
    }

    @Test(expected = IOException.class)
    public void when_thenThrow() throws IOException {
        OutputStream outputStream = PowerMockito.mock(OutputStream.class);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        //预设当流关闭时抛出异常
        PowerMockito.doThrow(new IOException()).when(outputStream).close();
        writer.flush();
        writer.close();
    }

    /**
     * 2.3 RETURNS_SMART_NULLS和RETURNS_DEEP_STUBS
     * RETURNS_SMART_NULLS实现了Answer接口的对象，它是创建mock对象时的一个可选参数，PowerMockito.mock(Class,Answer)。
     * 在创建mock对象时，有的方法我们没有进行stubbing，所以调用时会放回Null这样在进行操作是很可能抛出NullPointerException。
     * 如果通过RETURNS_SMART_NULLS参数创建的mock对象在没有调用stubbed方法时会返回SmartNull。例如：返回类型是String，
     * 会返回"";是int，会返回0；是List，会返回空的List。另外，在控制台窗口中可以看到SmartNull的友好提示。
     */
    @Test
    public void returnsSmartNullsTest() {
        List mock = PowerMockito.mock(List.class, RETURNS_SMART_NULLS);
        System.out.println("mock.get(0) = " + mock.get(0));

        //使用RETURNS_SMART_NULLS参数创建的mock对象，不会抛出NullPointerException异常。另外控制台窗口会提示信息“SmartNull returned by unstubbed get() method on mock”
        System.out.println("mock.toArray().length = " + mock.toArray().length);
    }

    /**
     * RETURNS_DEEP_STUBS也是创建mock对象时的备选参数
     * RETURNS_DEEP_STUBS参数程序会自动进行mock所需的对象，方法deepstubsTest和deepstubsTest2是等价的
     */
    @Test
    public void deepstubsTest() {
        Account account = PowerMockito.mock(Account.class, RETURNS_DEEP_STUBS);
        PowerMockito.when(account.getRailwayTicket().getDestination()).thenReturn("Beijing");
        account.getRailwayTicket().getDestination();
        Mockito.verify(account.getRailwayTicket()).getDestination();
        assertEquals("Beijing", account.getRailwayTicket().getDestination());
    }

    @Test
    public void deepstubsTest2() {
        Account account = PowerMockito.mock(Account.class);
        RailwayTicket railwayTicket = PowerMockito.mock(RailwayTicket.class);
        PowerMockito.when(account.getRailwayTicket()).thenReturn(railwayTicket);
        PowerMockito.when(railwayTicket.getDestination()).thenReturn("Beijing");

        account.getRailwayTicket().getDestination();
        Mockito.verify(account.getRailwayTicket()).getDestination();
        assertEquals("Beijing", account.getRailwayTicket().getDestination());
    }

    public class RailwayTicket {
        private String destination;

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }
    }

    public class Account {
        private RailwayTicket railwayTicket;

        public RailwayTicket getRailwayTicket() {
            return railwayTicket;
        }

        public void setRailwayTicket(RailwayTicket railwayTicket) {
            this.railwayTicket = railwayTicket;
        }
    }

    /**
     * 2.4 模拟方法体抛出异常
     */
    @Test(expected = RuntimeException.class)
    public void doThrow_PowerMockito() {
        List list = PowerMockito.mock(List.class);
        PowerMockito.doThrow(new RuntimeException()).when(list).add(1);
        list.add(1);
    }

    /**
     * 2.5 使用注解来快速模拟
     * 在上面的测试中我们在每个测试方法里都mock了一个List对象，为了避免重复的mock，是测试类更具有可读性，我们可以使用下面的注解方式来快速模拟对象：
     */
    @Mock
    private List mockList;

    @Test
    public void shorthand() {
        MockitoAnnotations.initMocks(this);
        mockList.add(1);
        Mockito.verify(mockList).add(1);
    }

    /**
     * 运行这个测试类你会发现报错了，mock的对象为NULL，为此我们必须在基类中添加初始化mock的代码
     * 或者在测试类加上 @RunWith(MockitoJUnitRunner.class)注解
     */
    @Test
    public void shorthand_2() {
        MockitoAnnotations.initMocks(this);// 初始化
        mockList.add(1);
        Mockito.verify(mockList).add(1);
    }

    /**
     * 2.6 参数匹配
     */
    @Test
    public void with_arguments() {
        Comparable comparable = PowerMockito.mock(Comparable.class);
        //预设根据不同的参数返回不同的结果
        PowerMockito.when(comparable.compareTo("Test")).thenReturn(1);
        PowerMockito.when(comparable.compareTo("Omg")).thenReturn(2);
        assertEquals(1, comparable.compareTo("Test"));
        assertEquals(2, comparable.compareTo("Omg"));
        //对于没有预设的情况会返回默认值
        assertEquals(0, comparable.compareTo("Not stub"));
    }

    /**
     * 除了匹配制定参数外，还可以匹配自己想要的任意参数
     */
    @Test
    public void with_unspecified_arguments() {
        List list = PowerMockito.mock(List.class);

        //匹配任意参数
        PowerMockito.when(list.get(anyInt())).thenReturn(1);
        assertEquals(1, list.get(1));
        assertEquals(1, list.get(999));

        PowerMockito.when(list.contains(argThat(new IsValid()))).thenReturn(true);
        assertTrue(list.contains(1));
        assertTrue(!list.contains(3));
    }

    private class IsValid extends ArgumentMatcher<List> {
        @Override
        public boolean matches(Object o) {
            return (Integer) o == 1 || (Integer) o == 2;
        }
    }

    /**
     * 注意：如果你使用了参数匹配，那么所有的参数都必须通过matchers来匹配，如下代码：
     */
    @Test
    public void all_arguments_provided_by_matchers() {
        Comparator comparator = PowerMockito.mock(Comparator.class);
        comparator.compare("nihao", "hello");
        //如果你使用了参数匹配，那么所有的参数都必须通过matchers来匹配
        Mockito.verify(comparator).compare(anyString(), eq("hello"));
        //下面的为无效的参数匹配使用
        //        Mockito.verify(comparator).compare(anyString(),"hello");
    }

    /**
     * 2.7 自定义参数匹配
     */
    /*@Test
    public void argumentMatchersTest() {
        //创建mock对象
        List<String> mock = PowerMockito.mock(List.class);

        //argThat(Matches<T> matcher)方法用来应用自定义的规则，可以传入任何实现Matcher接口的实现类。
        PowerMockito.when(mock.addAll(argThat(new IsListofTwoElements()))).thenReturn(true);

        mock.addAll(Arrays.asList("one", "two", "three"));
        //IsListofTwoElements用来匹配size为2的List，因为例子传入List为三个元素，所以此时将失败。
        Mockito.verify(mock).addAll(argThat(new IsListofTwoElements()));
    }

    class IsListofTwoElements extends ArgumentMatcher<List> {
        public boolean matches(Object list) {
            return ((List) list).size() == 2;
        }
    }*/

    /**
     * 2.8 捕获参数来进一步断言 用途：用参数捕获器来捕获传入方法的参数进行验证
     * ArgumentCaptor使用方法参考地址：https://www.jianshu.com/p/adee7d28cb59
     */
    @Test
    public void capturing_args() {
        PersonDao personDao = PowerMockito.mock(PersonDao.class);
        PersonService personService = new PersonService(personDao);

        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        personService.update(1, "jack");
        Mockito.verify(personDao).update(argument.capture());
        assertEquals(1, argument.getValue().getId());
        assertEquals("jack", argument.getValue().getName());
    }

    class Person {
        private int    id;
        private String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    interface PersonDao {
        public void update (Person person);
    }

    class PersonService {
        private PersonDao personDao;

        PersonService(PersonDao personDao) {
            this.personDao = personDao;
        }

        public void update(int id, String name) {
            personDao.update(new Person(id, name));
        }
    }

    /**
     * 2.9 使用方法预期回调接口生成期望值（Answer结构）
     *
     * 在单元测试中，有assert来判断测试的结果，verfiy来判断执行的次数和顺序，doAnswer用来判断执行的方法和方法的参数。
     * doAnswer一般和when配合使用，当条件满足是，执行对应的Answer的answer方法，如果answer方法抛出异常，那么测试不通过。
     */
    @Test
    public void answerTest() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.when(mockList.get(anyInt())).thenAnswer(new CustomAnswer());
        assertEquals("hello world:0", mockList.get(0));
        assertEquals("hello world:999", mockList.get(999));
    }

    private class CustomAnswer implements Answer<String> {
        public String answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments() ; // mock对象调用方法时传入的各个参数值
            return "hello world:" + args[0]; // 返回给mock对象调用方法后结果
        }
    }

    /**
     * 2.10 修改对未预设的调用返回默认期望
     */
    @Test
    public void unstubbed_invocations(){
        //mock对象使用Answer来对未预设的调用返回默认期望值
        List mock = PowerMockito.mock(List.class, new Answer() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return 999;
            }
        });
        //下面的get(1)没有预设，通常情况下会返回NULL，但是使用了Answer改变了默认期望值
        assertEquals(999, mock.get(1));
        //下面的size()没有预设，通常情况下会返回0，但是使用了Answer改变了默认期望值
        assertEquals(999, mock.size());
    }

    /**
     * 2.11 用spy监控真实对象<br>
     * Mock不是真实的对象，它只是用类型的class创建了一个虚拟对象，并可以设置对象行为 <br>
     * Spy是一个真实的对象，但它可以设置对象行为 <br>
     * InjectMocks创建这个类的对象并自动将标记@Mock、@Spy等注解的属性值注入到这个中
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void spy_on_real_objects(){
        List list = new LinkedList();
        List spy =  PowerMockito.spy(list);
        //下面预设的spy.get(0)会报错，因为会调用真实对象的get(0)，所以会抛出越界异常
        //PowerMockito.when(spy.get(0)).thenReturn(3);

        //使用doReturn-when可以避免when-thenReturn调用真实对象api
        PowerMockito.doReturn(999).when(spy).get(999);
        //预设size()期望值
        PowerMockito.when(spy.size()).thenReturn(100);
        //调用真实对象的api
        spy.add(1);
        spy.add(2);
        assertEquals(100, spy.size());
        assertEquals(1, spy.get(0));
        assertEquals(2, spy.get(1));
        Mockito.verify(spy).add(1);
        Mockito.verify(spy).add(2);
        assertEquals(999, spy.get(999));
        spy.get(2);
    }

    /**
     * 2.12 真实的部分mock
     */
    @Test
    public void real_partial_PowerMockito() {
        //通过spy来调用真实的api
        List list = spy(new ArrayList());
        assertEquals(0, list.size());

        //通过thenCallRealMethod来调用真实的api
        A a = PowerMockito.mock(A.class);
        PowerMockito.when(a.doSomething(anyInt())).thenCallRealMethod();
        assertEquals(999, a.doSomething(999));
    }

    class A {
        public int doSomething(int i) {
            return i;
        }
    }
//
//    /**
//     * 2.13 重置mock
//     */
//    @Test
//    public void reset_PowerMockito.mock(){
//        List list = PowerMockito.mock(List.class);
//        PowerMockito.when(list.size()).thenReturn(10);
//        list.add(1);
//        assertEquals(10,list.size());
//        //重置mock，清除所有的互动和预设
//        reset(list);
//        assertEquals(0,list.size());
//    }
//
//    /**
//     * 2.14 验证确切的调用次数
//     */
//    @Test
//    public void verifying_number_of_invocations() {
//        List list = PowerMockito.mock(List.class);
//        list.add(1);
//        list.add(2);
//        list.add(2);
//        list.add(3);
//        list.add(3);
//        list.add(3);
//        //验证是否被调用一次，等效于下面的times(1)
//        Mockito.verify(list).add(1);
//        Mockito.verify(list, times(1)).add(1);
//        //验证是否被调用2次
//        Mockito.verify(list, times(2)).add(2);
//        //验证是否被调用3次
//        Mockito.verify(list, times(3)).add(3);
//        //验证是否从未被调用过
//        Mockito.verify(list, Mockito.never()).add(4);
//        //验证至少调用一次
//        Mockito.verify(list, atLeastOnce()).add(1);
//        //验证至少调用2次
//        Mockito.verify(list, Mockito.atLeast(2)).add(2);
//        //验证至多调用3次
//        Mockito.verify(list, Mockito.atMost(3)).add(3);
//    }
//
//    /**
//     * 2.15 连续调用
//     */
//    @Test(expected = RuntimeException.class)
//    public void consecutive_calls() {
//        //模拟连续调用返回期望值，如果分开，则只有最后一个有效
//        PowerMockito.when(mockList.get(0)).thenReturn(0);
//        PowerMockito.when(mockList.get(0)).thenReturn(1);
//        PowerMockito.when(mockList.get(0)).thenReturn(2);
//        PowerMockito.when(mockList.get(1)).thenReturn(0).thenReturn(1).thenThrow(new RuntimeException());
//        assertEquals(2, mockList.get(0));
//        assertEquals(0, mockList.get(1));
//        assertEquals(1, mockList.get(1));
//        //第三次或更多调用都会抛出异常
//        mockList.get(1);
//    }
//
//    /**
//     * 2.16 验证执行顺序
//     */
//    @Test
//    public void verification_in_order() {
//        List list = PowerMockito.mock(List.class);
//        List list2 = PowerMockito.mock(List.class);
//        list.add(1);
//        list2.add("hello");
//        list.add(2);
//        list2.add("world");
//        //将需要排序的mock对象放入InOrder
//        InOrder inOrder = inOrder(list, list2);
//        //下面的代码不能颠倒顺序，验证执行顺序
//        inOrder.Mockito.verify(list).add(1);
//        inOrder.Mockito.verify(list2).add("hello");
//        inOrder.Mockito.verify(list).add(2);
//        inOrder.Mockito.verify(list2).add("world");
//    }
//
//    /**
//     * 2.17 确保模拟对象上无互动发生
//     */
//    @Test
//    public void verify_interaction(){
//        List list = PowerMockito.mock(List.class);
//        List list2 = PowerMockito.mock(List.class);
//        List list3 = PowerMockito.mock(List.class);
//        list.add(1);
//        Mockito.verify(list).add(1);
//        Mockito.verify(list,Mockito.never()).add(2);
//        //验证零互动行为
//        verifyZeroInteractions(list2,list3);
//    }
//
//    /**
//     * 2.18 找出冗余的互动(即未被验证到的)
//     */
//    @Test(expected = NoInteractionsWanted.class)
//    public void find_redundant_interaction(){
//        List list = PowerMockito.mock(List.class);
//        list.add(1);
//        list.add(2);
//        Mockito.verify(list,times(2)).add(anyInt());
//        //检查是否有未被验证的互动行为，因为add(1)和add(2)都会被上面的anyInt()验证到，所以下面的代码会通过
//        verifyNoMoreInteractions(list);
//
//        List list2 = PowerMockito.mock(List.class);
//        list2.add(1);
//        list2.add(2);
//        Mockito.verify(list2).add(1);
//        //检查是否有未被验证的互动行为，因为add(2)没有被验证，所以下面的代码会失败抛出异常
//        verifyNoMoreInteractions(list2);
//    }

}
