package com.scc363.hospitalproject.utils;

import com.scc363.hospitalproject.datamodels.Session;

import java.util.ArrayList;

public class SessionCache
{
    public ArrayList<Session> sessions;

    public SessionCache()
    {
        sessions = new ArrayList<>();
    }


    public boolean hasSession(String sessionKey, String username)
    {
        return getSession(sessionKey, username) != null;
    }


    public Session getSession(String sessionKey, String username)
    {
        for (Session session : this.sessions)
        {
            if (session.getSessionID().equals(sessionKey) && session.getUser().equals(username))
            {
                return session;
            }
        }
        return null;
    }

    public Session getSessionByUser(String username)
    {
        for (Session session : sessions)
        {
            if (session.getUser().equals(username))
            {
                return session;
            }
        }
        return null;
    }

    public boolean createSession(Session session)
    {
        if (!hasSession(session.getSessionID(), session.getUser()))
        {
            this.sessions.add(session);
            return true;
        }
        return false;
    }

    public void destroySession(String sessionID, String username)
    {
        if (getSession(sessionID, username) != null)
        {
            int positionInBuffer = findSessionPosition(sessionID);
            if (positionInBuffer >= 0)
            {
                this.sessions.remove(positionInBuffer);
            }
        }
    }


    private int findSessionPosition(String sessionKey)
    {
        for (int i = 0; i < this.sessions.size(); i++)
        {
            if (this.sessions.get(i).getSessionID().equals(sessionKey))
            {
                return i;
            }
        }
        return -1;
    }


}
