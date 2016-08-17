package fr.inria.rsommerard.fougere.data.social;

import android.os.Build;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import fr.inria.rsommerard.fougere.BuildConfig;

/**
 * Created by Romain on 15/08/2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class SocialDataPoolTest {

    private SocialDataPool socialDataPool;

    @Before
    public void setup() {
        this.socialDataPool = new SocialDataPool(RuntimeEnvironment.application);
    }

    @Test
    public void getAllData() {
        SocialData data1 = SocialDataProducer.produce();
        this.socialDataPool.insert(data1);

        SocialData data2 = SocialDataProducer.produce();
        this.socialDataPool.insert(data2);

        Assert.assertEquals(2, this.socialDataPool.getAll().size());
    }

    @Test
    public void insertAValidData() {
        SocialData data = SocialDataProducer.produce();
        this.socialDataPool.insert(data);

        Assert.assertEquals(1, this.socialDataPool.getAll().size());
    }

    @Test
    public void insertAValidDataTwoTimes() {
        SocialData data = SocialDataProducer.produce();
        this.socialDataPool.insert(data);
        this.socialDataPool.insert(data);

        Assert.assertEquals(1, this.socialDataPool.getAll().size());
    }

    @Test
    public void insertTwoValidData() {
        SocialData data1 = SocialDataProducer.produce();
        this.socialDataPool.insert(data1);

        SocialData data2 = SocialDataProducer.produce();
        this.socialDataPool.insert(data2);

        Assert.assertEquals(2, this.socialDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNullId() {
        SocialData data1 = SocialDataProducer.produce();
        this.socialDataPool.insert(data1);

        SocialData data2 = new SocialData(null, data1.getIdentifier(), data1.getContent(),
                data1.getTtl(), data1.getDisseminate(), data1.getSent());
        this.socialDataPool.insert(data2);

        Assert.assertEquals(1, this.socialDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNullIdentifier() {
        SocialData data = SocialDataProducer.produce();
        SocialData data1 = new SocialData(null, null, data.getContent(), data.getTtl(),
                data.getDisseminate(), data.getSent());
        this.socialDataPool.insert(data1);

        Assert.assertEquals(0, this.socialDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNullContent() {
        SocialData data = SocialDataProducer.produce();
        SocialData data1 = new SocialData(null, data.getIdentifier(), null, data.getTtl(),
                data.getDisseminate(), data.getSent());
        this.socialDataPool.insert(data1);

        Assert.assertEquals(0, this.socialDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNegativeTtl() {
        SocialData data = SocialDataProducer.produce();
        SocialData data1 = new SocialData(null, data.getIdentifier(), data.getContent(), -1,
                data.getDisseminate(), data.getSent());
        this.socialDataPool.insert(data1);

        Assert.assertEquals(0, this.socialDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNegativeDisseminate() {
        SocialData data = SocialDataProducer.produce();
        SocialData data1 = new SocialData(null, data.getIdentifier(), data.getContent(),
                data.getTtl(), -1, data.getSent());
        this.socialDataPool.insert(data1);

        Assert.assertEquals(0, this.socialDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNegativeSent() {
        SocialData data = SocialDataProducer.produce();
        SocialData data1 = new SocialData(null, data.getIdentifier(), data.getContent(),
                data.getTtl(), data.getDisseminate(), -1);
        this.socialDataPool.insert(data1);

        Assert.assertEquals(0, this.socialDataPool.getAll().size());
    }

    @Test
    public void updateAData() {
        SocialData data = SocialDataProducer.produce();
        this.socialDataPool.insert(data);

        data.setContent("Modified!");
        this.socialDataPool.update(data);

        Assert.assertEquals("Modified!", this.socialDataPool.getAll().get(0).getContent());
    }

    @Test
    public void deleteAValidData() {
        SocialData data = SocialDataProducer.produce();
        this.socialDataPool.insert(data);

        this.socialDataPool.delete(data);
        Assert.assertEquals(0, this.socialDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNullId() {
        SocialData data = SocialDataProducer.produce();
        this.socialDataPool.insert(data);

        SocialData data1 = new SocialData(null, data.getIdentifier(), data.getContent(),
                data.getTtl(), data.getDisseminate(), data.getSent());
        this.socialDataPool.delete(data1);

        Assert.assertEquals(0, this.socialDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNullIdentifier() {
        SocialData data = SocialDataProducer.produce();
        this.socialDataPool.insert(data);

        SocialData data1 = new SocialData(null, null, data.getContent(), data.getTtl(),
                data.getDisseminate(), data.getSent());
        this.socialDataPool.delete(data1);

        Assert.assertEquals(1, this.socialDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNullContent() {
        SocialData data = SocialDataProducer.produce();
        this.socialDataPool.insert(data);

        SocialData data1 = new SocialData(null, data.getIdentifier(), null, data.getTtl(),
                data.getDisseminate(), data.getSent());
        this.socialDataPool.delete(data1);

        Assert.assertEquals(0, this.socialDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNegativeTtl() {
        SocialData data = SocialDataProducer.produce();
        this.socialDataPool.insert(data);

        SocialData data1 = new SocialData(null, data.getIdentifier(), data.getContent(), -1,
                data.getDisseminate(), data.getSent());
        this.socialDataPool.delete(data1);

        Assert.assertEquals(0, this.socialDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNegativeDisseminate() {
        SocialData data = SocialDataProducer.produce();
        this.socialDataPool.insert(data);

        SocialData data1 = new SocialData(null, data.getIdentifier(), data.getContent(),
                data.getTtl(), -1, data.getSent());
        this.socialDataPool.delete(data1);

        Assert.assertEquals(0, this.socialDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNegativeSent() {
        SocialData data = SocialDataProducer.produce();
        this.socialDataPool.insert(data);

        SocialData data1 = new SocialData(null, data.getIdentifier(), data.getContent(),
                data.getTtl(), data.getDisseminate(), data.getSent());
        this.socialDataPool.delete(data1);

        Assert.assertEquals(0, this.socialDataPool.getAll().size());
    }
}
