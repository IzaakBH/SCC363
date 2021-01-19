package com.scc363.hospitalproject.services;

import com.scc363.hospitalproject.datamodels.Session;

import java.util.ArrayList;

public class SessionCache
{
    public ArrayList<Session> sessions;

    public SessionCache()
    {
        sessions = new ArrayList<>();
    }


    public boolean hasSession(String sessionKey)
    {
        return getSession(sessionKey) != null;
    }

    public Session getSession(String sessionKey)
    {
        for (Session session : this.sessions)
        {
            if (session.getSessionKey().equals(sessionKey))
            {
                return session;
            }
        }
        return null;
    }

    public boolean createSession(Session session)
    {
        if (!hasSession(session.getSessionKey()))
        {
            this.sessions.add(session);
            return true;
        }
        return false;
    }

    public void destroySession(String sessionKey)
    {
        if (getSession(sessionKey) != null)
        {
            int positionInBuffer = findSessionPosition(sessionKey);
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
            if (this.sessions.get(i).getSessionKey().equals(sessionKey))
            {
                return i;
            }
        }
        return -1;
    }
}
