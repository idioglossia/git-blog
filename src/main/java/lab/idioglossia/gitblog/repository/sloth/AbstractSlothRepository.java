package lab.idioglossia.gitblog.repository.sloth;

import lab.idioglossia.jsonsloth.JsonSlothManager;

import java.util.ArrayList;
import java.util.List;

public class AbstractSlothRepository<K, E> {
    private final Class<E> eClass;
    private final JsonSlothManager jsonSlothManager;

    public AbstractSlothRepository(Class<E> eClass, JsonSlothManager jsonSlothManager) {
        this.eClass = eClass;
        this.jsonSlothManager = jsonSlothManager;
    }

    public void save(E e){
        jsonSlothManager.save(e);
    }

    public E get(K k){
        return jsonSlothManager.get(k, eClass);
    }

    public int size(){
        return jsonSlothManager.size(eClass);
    }

    public List<K> keys(){
        return jsonSlothManager.getKeys(eClass);
    }

    public List<E> get(int f, int t){
        List<E> list = new ArrayList<>();
        for(int i = f; i <= t; i++){
            E e = jsonSlothManager.get(i, eClass);
            if(e != null)
                list.add(e);
        }
        return list;
    }
}
