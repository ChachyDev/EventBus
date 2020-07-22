package dev.deamsy.eventbus.impl.asm;

import dev.deamsy.eventbus.api.listener.EventListener;
import dev.deamsy.eventbus.impl.ListenerEventBus;
import dev.deamsy.eventbus.impl.listener.Listener;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.function.Consumer;

public class ASMEventBus extends ListenerEventBus {
    private final ASMClassLoader classLoader = new ASMClassLoader();

    @Override
    @SneakyThrows
    protected @NotNull Listener<?> createListener(@NotNull Method method, @Nullable Object obj, int id) {
        boolean isStatic = Modifier.isStatic(method.getModifiers());

        ClassNode classNode = new ClassNode();
        classNode.name = "EventListener " + id + " for ASMEventBus with hash code " + hashCode();
        classNode.superName = BaseASMEventListener.class.getName().replace('.', '/');
        classNode.access = Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER;
        classNode.version = Opcodes.V1_8;
        classNode.methods = new ArrayList<>();
        classNode.methods.add(createMethod("<init>", "(Ljava/lang/Class;ILjava/lang/reflect/Method;Ljava/lang/Object;)V", list -> {
            list.add(new VarInsnNode(Opcodes.ALOAD, 0));
            list.add(new VarInsnNode(Opcodes.ALOAD, 1));
            list.add(new VarInsnNode(Opcodes.ILOAD, 2));
            list.add(new VarInsnNode(Opcodes.ALOAD, 3));
            list.add(new VarInsnNode(Opcodes.ALOAD, 4));
            list.add(new MethodInsnNode(
                    Opcodes.INVOKESPECIAL,
                    "dev/deamsy/eventbus/impl/asm/BaseASMEventListener",
                    "<init>",
                    "(Ljava/lang/Class;ILjava/lang/reflect/Method;Ljava/lang/Object;)V"
            ));
            list.add(new InsnNode(Opcodes.RETURN));
        }));

        classNode.methods.add(createMethod("call", "(Ljava/lang/Object;)V", list -> {
            if (!isStatic) {
                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new FieldInsnNode(
                        Opcodes.GETFIELD,
                        BaseASMEventListener.class.getName().replace('.', '/'),
                        "obj",
                        "Ljava/lang/Object;"
                ));
                list.add(new TypeInsnNode(Opcodes.CHECKCAST,
                        method.getDeclaringClass().getName().replace('.', '/')));
            }
            list.add(new VarInsnNode(Opcodes.ALOAD, 1));
            list.add(new TypeInsnNode(Opcodes.CHECKCAST,
                    method.getParameterTypes()[0].getName().replace('.', '/')));
            int opcode = isStatic ? Opcodes.INVOKESTATIC : Opcodes.INVOKEVIRTUAL;
            list.add(new MethodInsnNode(
                    opcode,
                    method.getDeclaringClass().getName().replace('.', '/'),
                    method.getName(),
                    Type.getMethodDescriptor(method)
            ));
            list.add(new InsnNode(Opcodes.RETURN));
        }));

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        Class<?> c = classLoader.load(classNode.name, writer.toByteArray());
        return (BaseASMEventListener) c.getConstructor(Class.class, int.class, Method.class, Object.class).newInstance(
                method.getParameterTypes()[0],
                method.getDeclaredAnnotation(EventListener.class).value(),
                method,
                obj
        );
    }

    private MethodNode createMethod(String name, String desc, Consumer<InsnList> builder) {
        MethodNode node = new MethodNode();
        node.name = name;
        node.access = Opcodes.ACC_PUBLIC;
        node.desc = desc;
        InsnList list = new InsnList();
        builder.accept(list);
        node.instructions = list;
        return node;
    }

    private static class ASMClassLoader extends ClassLoader {
        public Class<?> load(String name, byte[] content) {
            return super.defineClass(name, content, 0, content.length);
        }
    }
}
