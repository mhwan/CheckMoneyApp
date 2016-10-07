package com.app.checkmoney.Items;

/**
 * Created by Mhwan on 2016. 9. 6..
 */
public class ContactUserItem extends UserItem{
    private int id;
    private boolean ischecked = false;
    private long photo_id=0, person_id=0;
    private boolean isenabled = true;

    public ContactUserItem(String name, String phnum){
        super(name, phnum);
    }

    public ContactUserItem(int id, String name, String phnum) {
        super(name, phnum);
        this.id = id;
    }

    public ContactUserItem(int id, boolean ischecked, String name, String phnum) {
        super(name, phnum);
        this.id = id;
        this.ischecked = ischecked;
    }

    public boolean isenabled() {
        return isenabled;
    }

    public void setIsenabled(boolean isenabled) {
        this.isenabled = isenabled;
    }

    public long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(long person_id) {
        this.person_id = person_id;
    }

    public long getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(long photo_id) {
        this.photo_id = photo_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }


    @Override
    public int hashCode() {
        return getPhoneNumber().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ContactUserItem)
            return getPhoneNumber().equals(((ContactUserItem) o).getPhoneNumber());

        return false;
    }
}
