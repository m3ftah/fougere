package fr.inria.rsommerard.fougere.data.contextual;

import android.os.Build;
import android.support.compat.BuildConfig;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by Romain on 15/08/2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ContextualDataPoolTest {

    private ContextualDataPool contextualDataPool;

    @Before
    public void setup() {
        this.contextualDataPool = new ContextualDataPool(RuntimeEnvironment.application);
    }

    @Test
    public void getAllData() {
        ContextualData data1 = ContextualDataProducer.produce();
        this.contextualDataPool.insert(data1);

        ContextualData data2 = ContextualDataProducer.produce();
        this.contextualDataPool.insert(data2);

        Assert.assertEquals(2, this.contextualDataPool.getAll().size());
    }

    @Test
    public void insertAValidData() {
        ContextualData data = ContextualDataProducer.produce();
        this.contextualDataPool.insert(data);

        Assert.assertEquals(1, this.contextualDataPool.getAll().size());
    }

    @Test
    public void insertAValidDataTwoTimes() {
        ContextualData data = ContextualDataProducer.produce();
        this.contextualDataPool.insert(data);
        this.contextualDataPool.insert(data);

        Assert.assertEquals(1, this.contextualDataPool.getAll().size());
    }

    @Test
    public void insertTwoValidData() {
        ContextualData data1 = ContextualDataProducer.produce();
        this.contextualDataPool.insert(data1);

        ContextualData data2 = ContextualDataProducer.produce();
        this.contextualDataPool.insert(data2);

        Assert.assertEquals(2, this.contextualDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNullId() {
        ContextualData data1 = ContextualDataProducer.produce();
        this.contextualDataPool.insert(data1);

        ContextualData data2 = new ContextualData(null, data1.getIdentifier(), data1.getContent(),
                data1.getTtl(), data1.getDisseminate(), data1.getSent());
        this.contextualDataPool.insert(data2);

        Assert.assertEquals(1, this.contextualDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNullIdentifier() {
        ContextualData data = ContextualDataProducer.produce();
        ContextualData data1 = new ContextualData(null, null, data.getContent(), data.getTtl(),
                data.getDisseminate(), data.getSent());
        this.contextualDataPool.insert(data1);

        Assert.assertEquals(0, this.contextualDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNullContent() {
        ContextualData data = ContextualDataProducer.produce();
        ContextualData data1 = new ContextualData(null, data.getIdentifier(), null, data.getTtl(),
                data.getDisseminate(), data.getSent());
        this.contextualDataPool.insert(data1);

        Assert.assertEquals(0, this.contextualDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNegativeTtl() {
        ContextualData data = ContextualDataProducer.produce();
        ContextualData data1 = new ContextualData(null, data.getIdentifier(), data.getContent(), -1,
                data.getDisseminate(), data.getSent());
        this.contextualDataPool.insert(data1);

        Assert.assertEquals(0, this.contextualDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNegativeDisseminate() {
        ContextualData data = ContextualDataProducer.produce();
        ContextualData data1 = new ContextualData(null, data.getIdentifier(), data.getContent(), data.getTtl(), -1,
                data.getSent());
        this.contextualDataPool.insert(data1);

        Assert.assertEquals(0, this.contextualDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNegativeSent() {
        ContextualData data = ContextualDataProducer.produce();
        ContextualData data1 = new ContextualData(null, data.getIdentifier(), data.getContent(), data.getTtl(),
                data.getDisseminate(), -1);
        this.contextualDataPool.insert(data1);

        Assert.assertEquals(0, this.contextualDataPool.getAll().size());
    }

    @Test
    public void updateAData() {
        ContextualData data = ContextualDataProducer.produce();
        this.contextualDataPool.insert(data);

        data.setContent("Modified!");
        this.contextualDataPool.update(data);

        Assert.assertEquals("Modified!", this.contextualDataPool.getAll().get(0).getContent());
    }

    @Test
    public void deleteAValidData() {
        ContextualData data = ContextualDataProducer.produce();
        this.contextualDataPool.insert(data);

        this.contextualDataPool.delete(data);
        Assert.assertEquals(0, this.contextualDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNullId() {
        ContextualData data = ContextualDataProducer.produce();
        this.contextualDataPool.insert(data);

        ContextualData data1 = new ContextualData(null, data.getIdentifier(), data.getContent(),
                data.getTtl(), data.getDisseminate(), data.getSent());
        this.contextualDataPool.delete(data1);

        Assert.assertEquals(0, this.contextualDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNullIdentifier() {
        ContextualData data = ContextualDataProducer.produce();
        this.contextualDataPool.insert(data);

        ContextualData data1 = new ContextualData(null, null, data.getContent(), data.getTtl(),
                data.getDisseminate(), data.getSent());
        this.contextualDataPool.delete(data1);

        Assert.assertEquals(1, this.contextualDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNullContent() {
        ContextualData data = ContextualDataProducer.produce();
        this.contextualDataPool.insert(data);

        ContextualData data1 = new ContextualData(null, data.getIdentifier(), null, data.getTtl(),
                data.getDisseminate(), data.getSent());
        this.contextualDataPool.delete(data1);

        Assert.assertEquals(0, this.contextualDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNegativeTtl() {
        ContextualData data = ContextualDataProducer.produce();
        this.contextualDataPool.insert(data);

        ContextualData data1 = new ContextualData(null, data.getIdentifier(), data.getContent(), -1,
                data.getDisseminate(), data.getSent());
        this.contextualDataPool.delete(data1);

        Assert.assertEquals(0, this.contextualDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNegativeDisseminate() {
        ContextualData data = ContextualDataProducer.produce();
        this.contextualDataPool.insert(data);

        ContextualData data1 = new ContextualData(null, data.getIdentifier(), data.getContent(),
                data.getTtl(), -1, data.getSent());
        this.contextualDataPool.delete(data1);

        Assert.assertEquals(0, this.contextualDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNegativeSent() {
        ContextualData data = ContextualDataProducer.produce();
        this.contextualDataPool.insert(data);

        ContextualData data1 = new ContextualData(null, data.getIdentifier(), data.getContent(),
                data.getTtl(), data.getDisseminate(), data.getSent());
        this.contextualDataPool.delete(data1);

        Assert.assertEquals(0, this.contextualDataPool.getAll().size());
    }
}
